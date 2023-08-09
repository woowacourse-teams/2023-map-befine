package com.mapbefine.mapbefine.topic.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TopicCommandServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicCommandService topicCommandService;

    @Autowired
    private TopicQueryService topicQueryService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("비어있는 토픽을 생성할 수 있다.")
    public void saveEmptyTopic_Success() {
        //given
        AuthMember user = MemberFixture.createUser(member);

        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        Collections.emptyList()
                );

        //when
        Long topicId = topicCommandService.saveTopic(user, request);

        //then
        TopicDetailResponse detail = topicQueryService.findDetailById(user, topicId);

        assertThat(detail.id()).isEqualTo(topicId);
        assertThat(detail.name()).isEqualTo(request.name());
        assertThat(detail.description()).isEqualTo(request.description());
        assertThat(detail.pinCount()).isEqualTo(request.pins().size());
    }

    @Test
    @DisplayName("Guest는 비어있는 토픽을 생성할 수 없다.")
    public void saveEmptyTopic_Fail() {
        //given
        AuthMember guest = new Guest();
        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        Collections.emptyList()
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.saveTopic(guest, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Guest는 토픽을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("핀을 통해 새로운 토픽을 생성할 수 있다.")
    public void saveTopicWithPins_Success() {
        //given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);

        Pin pin1 = PinFixture.create(location, topic, member);
        Pin pin2 = PinFixture.create(location, topic, member);

        topicRepository.save(topic);

        //when
        AuthMember user = MemberFixture.createUser(member);
        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        List.of(pin1.getId(), pin2.getId())
                );

        Long topicId = topicCommandService.saveTopic(user, request);

        //then
        TopicDetailResponse detail = topicQueryService.findDetailById(user, topicId);

        assertThat(detail.pinCount()).isEqualTo(2);
        assertThat(detail.pins()).extractingResultOf("name")
                .containsExactlyInAnyOrder(
                        pin1.getPinInfo().getName(),
                        pin2.getPinInfo().getName()
                );
        assertThat(detail.name()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("Guest는 핀을 통해 새로운 토픽을 생성할 수 없다.")
    public void saveTopicWithPins_Fail1() {
        //given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);

        Pin pin1 = PinFixture.create(location, topic, member);
        Pin pin2 = PinFixture.create(location, topic, member);

        topicRepository.save(topic);

        AuthMember guest = new Guest();
        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        List.of(pin1.getId(), pin2.getId())
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.saveTopic(guest, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Guest는 토픽을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("권한이 없는 핀을 통해 토픽을 생성할 수 없다.")
    public void saveTopicWithPins_Fail2() {
        //given
        Member topicOwner = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.USER
        );
        memberRepository.save(topicOwner);

        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(topicOwner);

        Pin pin1 = PinFixture.create(location, topic, member);
        Pin pin2 = PinFixture.create(location, topic, member);

        topicRepository.save(topic);

        AuthMember user = MemberFixture.createUser(member);
        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        List.of(pin1.getId(), pin2.getId())
                );
        //when then
        assertThatThrownBy(() -> topicCommandService.saveTopic(user, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("복사할 수 없는 pin이 존재합니다.");
    }

    @Test
    @DisplayName("기존의 토픽들을 통해 새로운 토픽을 생성할 수 있다.")
    public void merge_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        Location location = LocationFixture.create();
        locationRepository.save(location);

        Pin pin1 = PinFixture.create(location, topic1, member);
        Pin pin2 = PinFixture.create(location, topic1, member);
        Pin pin3 = PinFixture.create(location, topic2, member);
        Pin pin4 = PinFixture.create(location, topic2, member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember user = MemberFixture.createUser(member);
        TopicMergeRequest request =
                TopicFixture.createPublicAndAllMembersMergeRequestWithTopics(
                        List.of(topic1.getId(), topic2.getId())
                );

        Long topicId = topicCommandService.merge(user, request);

        //then
        TopicDetailResponse detail = topicQueryService.findDetailById(user, topicId);

        assertThat(detail.pinCount()).isEqualTo(4);
        assertThat(detail.pins()).extractingResultOf("name")
                .containsExactlyInAnyOrder(
                        pin1.getPinInfo().getName(),
                        pin2.getPinInfo().getName(),
                        pin3.getPinInfo().getName(),
                        pin4.getPinInfo().getName()
                );
        assertThat(detail.name()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("Guest는 기존의 토픽들을 통해 새로운 토픽을 생성할 수 없다.")
    public void merge_Fail1() {
        //given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        topicRepository.save(topic);

        AuthMember guest = new Guest();
        TopicMergeRequest request =
                TopicFixture.createPublicAndAllMembersMergeRequestWithTopics(
                        List.of(topic.getId())
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.merge(guest, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Guest는 토픽을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("권한이 없는 토픽들을 통해 새로운 토픽을 생성할 수 없다.")
    public void merge_Fail2() {
        //given
        Member topicOwner = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.USER
        );
        memberRepository.save(topicOwner);

        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(topicOwner);
        topicRepository.save(topic);

        AuthMember user = MemberFixture.createUser(member);
        TopicMergeRequest request =
                TopicFixture.createPublicAndAllMembersMergeRequestWithTopics(
                        List.of(topic.getId())
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.merge(user, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("복사할 수 없는 토픽이 존재합니다.");
    }

    @Test
    @DisplayName("토픽의 정보를 수정할 수 있다.")
    public void updateTopicInfo_Success() {
        //given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        topicRepository.save(topic);

        //when
        assertThat(topic.getTopicInfo().getName()).isEqualTo("토픽 멤버만 읽을 수 있는 토픽");
        assertThat(topic.getTopicInfo().getDescription()).isEqualTo("토픽 멤버만 읽을 수 있습니다.");

        AuthMember user = MemberFixture.createUser(member);
        TopicUpdateRequest request = new TopicUpdateRequest(
                "수정된 이름",
                "https://map-befine-official.github.io/favicon.png",
                "수정된 설명",
                Publicity.PRIVATE,
                Permission.GROUP_ONLY
        );

        topicCommandService.updateTopicInfo(user, topic.getId(), request);

        //then
        TopicDetailResponse detail = topicQueryService.findDetailById(user, topic.getId());

        assertThat(detail.name()).isEqualTo("수정된 이름");
        assertThat(detail.description()).isEqualTo("수정된 설명");
    }

    @Test
    @DisplayName("권한이 없는 토픽의 정보를 수정할 수 없다.")
    public void updateTopicInfo_Fail() {
        //given
        Member topicOwner = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.USER
        );
        memberRepository.save(topicOwner);

        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(topicOwner);
        topicRepository.save(topic);

        //when then
        AuthMember user = MemberFixture.createUser(member);
        TopicUpdateRequest request = new TopicUpdateRequest(
                "수정된 이름",
                "https://map-befine-official.github.io/favicon.png",
                "수정된 설명",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS
        );

        assertThatThrownBy(() -> topicCommandService.updateTopicInfo(user, topic.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("업데이트 권한이 없습니다.");
    }

    @Test
    @DisplayName("Admin은 토픽을 삭제할 수 있다.")
    public void delete_Success() {
        //given
        Member admin = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.ADMIN
        );
        memberRepository.save(admin);

        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(admin);
        topicRepository.save(topic);

        //when
        AuthMember adminAuthMember = new Admin(admin.getId());

        assertThat(topic.isDeleted()).isFalse();

        topicCommandService.delete(adminAuthMember, topic.getId());

        //then
        Topic deletedTopic = topicRepository.findById(topic.getId()).get();

        assertThat(deletedTopic.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Admin이 아닌 경우, 토픽을 삭제할 수 없다.")
    public void delete_Fail() {
        //given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic);

        //when then
        AuthMember user = MemberFixture.createUser(member);

        assertThatThrownBy(() -> topicCommandService.delete(user, topic.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제 권한이 없습니다.");
    }
}
