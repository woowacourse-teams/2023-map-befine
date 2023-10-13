package com.mapbefine.mapbefine.topic.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.TestDatabaseContainer;
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
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicForbiddenException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TopicCommandServiceTest extends TestDatabaseContainer {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private TopicCommandService topicCommandService;

    @Autowired
    private TopicQueryService topicQueryService;

    private Member member;
    private Location location;
    private AuthMember user;
    private AuthMember guest;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        location = locationRepository.save(LocationFixture.create());

        user = MemberFixture.createUser(member);
        guest = new Guest();
    }

    @Test
    @DisplayName("비어있는 토픽을 생성할 수 있다.")
    void saveEmptyTopic_Success() {
        //given
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
    @DisplayName("이미지 없이 토픽을 생성하면 기본 이미지를 반환한다.")
    void saveEmptyTopicAndEmptyImage_Success() {
        //given
        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersAndEmptyImageCreateRequestWithPins(
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
        assertThat(detail.image()).isEqualTo(
                "https://velog.velcdn.com/images/semnil5202/post/37f3bcb9-0b07-4100-85f6-f1d5ad037c14/image.svg"
        );
    }

    @Test
    @DisplayName("Guest는 비어있는 토픽을 생성할 수 없다.")
    void saveEmptyTopic_Fail() {
        //given
        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        Collections.emptyList()
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.saveTopic(guest, request))
                .isInstanceOf(TopicForbiddenException.class);
    }

    @Test
    @DisplayName("핀을 통해 새로운 토픽을 생성할 수 있다.")
    void saveTopicWithPins_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);

        Pin pin1 = PinFixture.create(location, topic, member);
        Pin pin2 = PinFixture.create(location, topic, member);

        topicRepository.save(topic);

        //when
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
    void saveTopicWithPins_Fail1() {
        //given
        Topic publicAndAllMembersTopic = TopicFixture.createPublicAndAllMembersTopic(member);

        Pin pin1 = PinFixture.create(location, publicAndAllMembersTopic, member);
        Pin pin2 = PinFixture.create(location, publicAndAllMembersTopic, member);

        topicRepository.save(publicAndAllMembersTopic);

        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        List.of(pin1.getId(), pin2.getId())
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.saveTopic(guest, request))
                .isInstanceOf(TopicForbiddenException.class);
    }

    @Test
    @DisplayName("권한이 없는 핀을 통해 토픽을 생성할 수 없다.")
    void saveTopicWithPins_Fail2() {
        //given
        Member topicOwner = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.USER
        );
        memberRepository.save(topicOwner);

        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(topicOwner);

        Pin pin1 = PinFixture.create(location, topic, member);
        Pin pin2 = PinFixture.create(location, topic, member);

        topicRepository.save(topic);

        TopicCreateRequest request =
                TopicFixture.createPublicAndAllMembersCreateRequestWithPins(
                        List.of(pin1.getId(), pin2.getId())
                );
        //when then
        assertThatThrownBy(() -> topicCommandService.saveTopic(user, request))
                .isInstanceOf(PinBadRequestException.class);
    }

    @Test
    @DisplayName("기존의 토픽들을 통해 새로운 토픽을 생성할 수 있다.")
    void merge_Success() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        Pin pin1 = PinFixture.create(location, topic1, member);
        Pin pin2 = PinFixture.create(location, topic1, member);
        Pin pin3 = PinFixture.create(location, topic2, member);
        Pin pin4 = PinFixture.create(location, topic2, member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
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
    void merge_Fail1() {
        //given
        Topic privateAndGroupOnlyTopic = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        topicRepository.save(privateAndGroupOnlyTopic);

        TopicMergeRequest request =
                TopicFixture.createPublicAndAllMembersMergeRequestWithTopics(
                        List.of(privateAndGroupOnlyTopic.getId())
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.merge(guest, request))
                .isInstanceOf(TopicForbiddenException.class);
    }

    @Test
    @DisplayName("권한이 없는 토픽들을 통해 새로운 토픽을 생성할 수 없다.")
    void merge_Fail2() {
        //given
        Member topicOwner = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.USER
        );
        memberRepository.save(topicOwner);

        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(topicOwner);
        topicRepository.save(topic);

        TopicMergeRequest request =
                TopicFixture.createPublicAndAllMembersMergeRequestWithTopics(
                        List.of(topic.getId())
                );

        //when then
        assertThatThrownBy(() -> topicCommandService.merge(user, request))
                .isInstanceOf(TopicBadRequestException.class);
    }

    @Test
    @DisplayName("핀을 권한이 있는 토픽에 복사할 수 있다.")
    void copyPin_Success() {
        // given
        Topic source = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));

        List<Pin> sourcePins = List.of(
                PinFixture.create(location, source, member),
                PinFixture.create(location, source, member),
                PinFixture.create(location, source, member)
        );
        pinRepository.saveAll(sourcePins);

        List<Long> pinIds = sourcePins.stream()
                .map(Pin::getId)
                .toList();

        Topic target = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));

        // when
        topicCommandService.copyPin(user, target.getId(), pinIds);

        // then
        List<Pin> targetPins = target.getPins();
        Pin targetPin = targetPins.iterator().next();
        Pin sourcePin = sourcePins.get(0);

        assertThat(targetPins).hasSize(sourcePins.size());
        assertThat(targetPin.getId()).isNotEqualTo(sourcePin.getId());
        assertThat(targetPin.getPinInfo().getName()).isEqualTo(sourcePin.getPinInfo().getName());
    }

    @Test
    @DisplayName("토픽의 정보를 수정할 수 있다.")
    void updateTopicInfo_Success() {
        //given
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        topicRepository.save(topic);

        //when
        assertThat(topic.getTopicInfo().getName()).isEqualTo("토픽 회원만 읽을 수 있는 토픽");
        assertThat(topic.getTopicInfo().getDescription()).isEqualTo("토픽 회원만 읽을 수 있습니다.");

        AuthMember user = MemberFixture.createUser(member);
        TopicUpdateRequest request = new TopicUpdateRequest(
                "수정된 이름",
                "https://map-befine-official.github.io/favicon.png",
                "수정된 설명",
                Publicity.PRIVATE,
                PermissionType.GROUP_ONLY
        );

        topicCommandService.updateTopicInfo(user, topic.getId(), request);

        //then
        TopicDetailResponse detail = topicQueryService.findDetailById(user, topic.getId());

        assertThat(detail.name()).isEqualTo("수정된 이름");
        assertThat(detail.description()).isEqualTo("수정된 설명");
    }

    @Test
    @DisplayName("권한이 없는 토픽의 정보를 수정할 수 없다.")
    void updateTopicInfo_Fail() {
        //given
        Member topicOwner = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.USER
        );
        memberRepository.save(topicOwner);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(topicOwner);
        topicRepository.save(topic);

        //when then
        TopicUpdateRequest request = new TopicUpdateRequest(
                "수정된 이름",
                "https://map-befine-official.github.io/favicon.png",
                "수정된 설명",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS
        );

        assertThatThrownBy(() -> topicCommandService.updateTopicInfo(user, topic.getId(), request))
                .isInstanceOf(TopicForbiddenException.class);
    }

    @Test
    @DisplayName("Admin은 토픽을 삭제할 수 있다.")
    void delete_Success() {
        //given
        Member admin = MemberFixture.create(
                "topicOwner",
                "topicOwner@naver.com",
                Role.ADMIN
        );
        memberRepository.save(admin);

        Topic topic = TopicFixture.createPublicAndAllMembersTopic(admin);
        topicRepository.save(topic);

        //when
        AuthMember adminAuthMember = new Admin(admin.getId());

        assertThat(topic.isDeleted()).isFalse();

        topicCommandService.delete(adminAuthMember, topic.getId());

        //then
        assertThat(topicRepository.existsById(topic.getId())).isFalse();
    }

    @Test
    @DisplayName("Admin이 아닌 경우, 토픽을 삭제할 수 없다.")
    void delete_Fail() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);
        topicRepository.save(topic);

        //when then
        assertThatThrownBy(() -> topicCommandService.delete(user, topic.getId()))
                .isInstanceOf(TopicForbiddenException.class);
    }
}
