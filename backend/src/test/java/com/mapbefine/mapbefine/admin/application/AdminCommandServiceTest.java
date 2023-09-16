package com.mapbefine.mapbefine.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.domain.Status;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.PinImageFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class AdminCommandServiceTest {

    @Autowired
    private AdminCommandService adminCommandService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PinImageRepository pinImageRepository;

    @Autowired
    private AtlasRepository atlasRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private Location location;
    private Member admin;
    private Member member;
    private Topic topic;
    private Pin pin;
    private PinImage pinImage;

    @BeforeEach
    void setup() {
        admin = memberRepository.save(MemberFixture.create("Admin", "admin@naver.com", Role.ADMIN));
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
        location = locationRepository.save(LocationFixture.create());
        pin = pinRepository.save(PinFixture.create(location, topic, member));
        pinImage = pinImageRepository.save(PinImageFixture.create(pin));
    }

    @DisplayName("Member를 차단(탈퇴시킬)할 경우, Member가 생성한 토픽, 핀, 핀 이미지가 soft-deleting 된다.")
    @Test
    void blockMember_Success() {
        //given
        AuthMember adminAuthMember = MemberFixture.createUser(admin);
        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        Atlas atlas = Atlas.createWithAssociatedMember(topic, member);
        Permission permission = Permission.createPermissionAssociatedWithTopicAndMember(topic, member);

        bookmarkRepository.save(bookmark);
        atlasRepository.save(atlas);
        permissionRepository.save(permission);

        assertAll(() -> {
            assertThat(member.getMemberInfo().getStatus()).isEqualTo(Status.NORMAL);
            assertThat(topic.isDeleted()).isFalse();
            assertThat(pin.isDeleted()).isFalse();
            assertThat(pinImage.isDeleted()).isFalse();
            assertThat(bookmarkRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isTrue();
            assertThat(atlasRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isTrue();
            assertThat(permissionRepository.existsByTopicIdAndMemberId(topic.getId(), member.getId())).isTrue();
        });

        //when
        adminCommandService.blockMember(adminAuthMember, member.getId());

        //then
        Topic deletedTopic = topicRepository.findById(topic.getId()).get();
        Pin deletedPin = pinRepository.findById(pin.getId()).get();
        PinImage deletedPinImage = pinImageRepository.findById(pinImage.getId()).get();

        assertAll(() -> {
            assertThat(member.getMemberInfo().getStatus()).isEqualTo(Status.BLOCKED);
            assertThat(deletedTopic.isDeleted()).isTrue();
            assertThat(deletedPin.isDeleted()).isTrue();
            assertThat(deletedPinImage.isDeleted()).isTrue();
            assertThat(bookmarkRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isFalse();
            assertThat(atlasRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isFalse();
            assertThat(permissionRepository.existsByTopicIdAndMemberId(topic.getId(), member.getId())).isFalse();
        });
    }

    @DisplayName("Admin이 아닐 경우, Member를 차단(탈퇴시킬)할 수 없다.")
    @Test
    void blockMember_Fail() {
        //given
        AuthMember userAuthMember = MemberFixture.createUser(member);
        Member otherMember = MemberFixture.create("otherMember", "otherMember@email.com", Role.USER);
        memberRepository.save(otherMember);

        assertAll(() -> {
            assertThat(member.getMemberInfo().getStatus()).isEqualTo(Status.NORMAL);
            assertThat(topic.isDeleted()).isFalse();
            assertThat(pin.isDeleted()).isFalse();
            assertThat(pinImage.isDeleted()).isFalse();
        });

        //when then
        Long otherMemberId = otherMember.getId();
        assertThatThrownBy(() -> adminCommandService.blockMember(userAuthMember, otherMemberId))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @DisplayName("Admin은 토픽을 삭제시킬 수 있다.")
    @Test
    void deleteTopic_Success() {
        //given
        AuthMember adminAuthMember = MemberFixture.createUser(admin);

        assertThat(topic.isDeleted()).isFalse();

        //when
        adminCommandService.deleteTopic(adminAuthMember, topic.getId());

        //then
        Topic deletedTopic = topicRepository.findById(topic.getId()).get();

        assertThat(deletedTopic.isDeleted()).isTrue();
    }

    @DisplayName("Admin이 아닐 경우, 토픽을 삭제시킬 수 없다.")
    @Test
    void deleteTopic_Fail() {
        //given
        AuthMember userAuthMember = MemberFixture.createUser(member);

        assertThat(topic.isDeleted()).isFalse();

        //when then
        Long topicId = topic.getId();
        assertThatThrownBy(() -> adminCommandService.deleteTopic(userAuthMember, topicId))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @DisplayName("Admin은 토픽 이미지를 삭제할 수 있다.")
    @Test
    void deleteTopicImage_Success() {
        //given
        AuthMember adminAuthMember = MemberFixture.createUser(admin);
        TopicInfo topicInfo = topic.getTopicInfo();

        topic.updateTopicInfo(topicInfo.getName(), topicInfo.getDescription(), "https://imageUrl.png");

        assertThat(topic.getTopicInfo().getImageUrl()).isEqualTo("https://imageUrl.png");

        //when
        adminCommandService.deleteTopicImage(adminAuthMember, topic.getId());

        //then
        Topic imageDeletedTopic = topicRepository.findById(topic.getId()).get();

        assertThat(imageDeletedTopic.getTopicInfo().getImageUrl())
                .isEqualTo("https://map-befine-official.github.io/favicon.png");
    }

    @DisplayName("Admin이 아닐 경우, 이미지를 삭제할 수 없다.")
    @Test
    void deleteTopicImage_Fail() {
        //given
        AuthMember userAuthMember = MemberFixture.createUser(member);
        TopicInfo topicInfo = topic.getTopicInfo();

        topic.updateTopicInfo(topicInfo.getName(), topicInfo.getDescription(), "https://imageUrl.png");

        assertThat(topic.getTopicInfo().getImageUrl()).isEqualTo("https://imageUrl.png");

        //when then
        Long topicId = topic.getId();
        assertThatThrownBy(() -> adminCommandService.deleteTopicImage(userAuthMember, topicId))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @DisplayName("Admin은 핀을 삭제할 수 있다.")
    @Test
    void deletePin_Success() {
        //given
        AuthMember adminAuthMember = MemberFixture.createUser(admin);

        assertThat(pin.isDeleted()).isFalse();

        //when
        adminCommandService.deletePin(adminAuthMember, pin.getId());

        //then
        Pin deletedPin = pinRepository.findById(pin.getId()).get();

        assertThat(deletedPin.isDeleted()).isTrue();
    }

    @DisplayName("Admin이 아닐 경우, 핀을 삭제할 수 없다.")
    @Test
    void deletePin_Fail() {
        //given
        AuthMember userAuthMember = MemberFixture.createUser(member);

        assertThat(pin.isDeleted()).isFalse();

        //when then
        Long pinId = pin.getId();
        assertThatThrownBy(() -> adminCommandService.deletePin(userAuthMember, pinId))
                .isInstanceOf(PermissionForbiddenException.class);
    }

    @DisplayName("Admin인 경우, 핀 이미지를 삭제할 수 있다.")
    @Test
    void deletePinImage_Success() {
        //given
        AuthMember adminAuthMember = MemberFixture.createUser(admin);

        assertThat(pinImage.isDeleted()).isFalse();

        //when
        adminCommandService.deletePinImage(adminAuthMember, pinImage.getId());

        //then
        PinImage deletedPinImage = pinImageRepository.findById(pinImage.getId()).get();

        assertThat(deletedPinImage.isDeleted()).isTrue();
    }

    @DisplayName("Admin이 아닐 경우, 핀 이미지를 삭제할 수 없다.")
    @Test
    void deletePinImage_Fail() {
        //given
        AuthMember userAuthMember = MemberFixture.createUser(member);

        assertThat(pinImage.isDeleted()).isFalse();

        //when then
        Long pinImageId = pinImage.getId();
        assertThatThrownBy(() -> adminCommandService.deletePinImage(userAuthMember, pinImageId))
                .isInstanceOf(PermissionForbiddenException.class);
    }

}