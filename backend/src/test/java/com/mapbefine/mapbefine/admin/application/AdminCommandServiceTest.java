package com.mapbefine.mapbefine.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
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
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.PinImageFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@ServiceTest
class AdminCommandServiceTest extends TestDatabaseContainer {

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

    @Autowired
    TestEntityManager testEntityManager;

    private Member member;
    private Topic topic;
    private Pin pin;
    private PinImage pinImage;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
        Location location = locationRepository.save(LocationFixture.create());
        pin = pinRepository.save(PinFixture.create(location, topic, member));
        pinImage = pinImageRepository.save(PinImageFixture.create(pin));
    }

    @DisplayName("Member를 차단(탈퇴시킬)할 경우, Member가 생성한 토픽, 핀, 핀 이미지(soft delete)와 연관된 엔티티들을 삭제한다.")
    @Test
    void blockMember_Success() {
        //given
        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, member);
        Atlas atlas = Atlas.createWithAssociatedMember(topic, member);
        Permission permission = Permission.createPermissionAssociatedWithTopicAndMember(topic, member);

        bookmarkRepository.save(bookmark);
        atlasRepository.save(atlas);
        permissionRepository.save(permission);

        assertSoftly(softly -> {
            assertThat(member.getMemberInfo().getStatus()).isEqualTo(Status.NORMAL);
            assertThat(topic.isDeleted()).isFalse();
            assertThat(pin.isDeleted()).isFalse();
            assertThat(pinImage.isDeleted()).isFalse();
            assertThat(bookmarkRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isTrue();
            assertThat(atlasRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isTrue();
            assertThat(permissionRepository.existsByTopicIdAndMemberId(topic.getId(), member.getId())).isTrue();
        });

        //when
        testEntityManager.clear();
        adminCommandService.blockMember(member.getId());

        //then
        Member blockedMember = memberRepository.findById(member.getId()).get();

        assertSoftly(softly -> {
            assertThat(blockedMember.getMemberInfo().getStatus()).isEqualTo(Status.BLOCKED);
            assertThat(topicRepository.existsById(topic.getId())).isFalse();
            assertThat(pinRepository.existsById(pin.getId())).isFalse();
            assertThat(pinImageRepository.existsById(pinImage.getId())).isFalse();
            assertThat(bookmarkRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isFalse();
            assertThat(atlasRepository.existsByMemberIdAndTopicId(member.getId(), topic.getId())).isFalse();
            assertThat(permissionRepository.existsByTopicIdAndMemberId(topic.getId(), member.getId())).isFalse();
        });
    }

    @DisplayName("Admin은 토픽을 삭제시킬 수 있다.")
    @Test
    void deleteTopic_Success() {
        //given
        assertThat(topic.isDeleted()).isFalse();

        //when
        Long topicId = topic.getId();
        adminCommandService.deleteTopic(topicId);

        //then
        assertThat(topicRepository.existsById(topicId)).isFalse();
    }

    @DisplayName("Admin은 토픽 이미지를 삭제할 수 있다.")
    @Test
    void deleteTopicImage_Success() {
        //given
        topic.updateTopicImageUrl("https://imageUrl.png");

        assertThat(topic.getTopicInfo().getImageUrl()).isEqualTo("https://imageUrl.png");

        //when
        adminCommandService.deleteTopicImage(topic.getId());

        //then
        Topic imageDeletedTopic = topicRepository.findById(topic.getId()).get();

        assertThat(imageDeletedTopic.getTopicInfo().getImageUrl()).isEqualTo(
                "https://velog.velcdn.com/images/semnil5202/post/37f3bcb9-0b07-4100-85f6-f1d5ad037c14/image.svg"
        );
    }

    @DisplayName("Admin은 핀을 삭제할 수 있다.")
    @Test
    void deletePin_Success() {
        //given
        assertThat(pin.isDeleted()).isFalse();

        //when
        adminCommandService.deletePin(pin.getId());

        //then
        assertThat(pinRepository.existsById(pin.getId())).isFalse();
    }

    @DisplayName("핀 삭제 시, 토픽의 핀 개수를 1 감소시킨다.")
    @Test
    void deletePin_Success_decreaseTopicPinCount() {
        //given
        assertThat(pin.isDeleted()).isFalse();
        int pinCountBeforeDelete = topic.getPinCount();

        //when
        adminCommandService.deletePin(pin.getId());

        //then
        assertThat(topic.getPinCount()).isEqualTo(pinCountBeforeDelete - 1);
    }

    @DisplayName("Admin인 경우, 핀 이미지를 삭제할 수 있다.")
    @Test
    void deletePinImage_Success() {
        //given
        assertThat(pinImage.isDeleted()).isFalse();

        //when
        adminCommandService.deletePinImage(pinImage.getId());

        //then
        assertThat(pinImageRepository.findById(pinImage.getId())).isEmpty();
    }

}
