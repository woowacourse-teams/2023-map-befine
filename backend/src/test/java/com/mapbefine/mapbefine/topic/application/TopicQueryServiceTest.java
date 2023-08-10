package com.mapbefine.mapbefine.topic.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
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
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TopicQueryServiceTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private TopicQueryService topicQueryService;
    private Member member;

    @BeforeEach
    void setup() {
        topicQueryService = new TopicQueryService(pinRepository, topicRepository);

        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("읽을 수 있는 모든 Topic 목록을 조회한다.")
    void findAllReadable_Success1() {
        //given
        saveAllReadableTopicOfCount(1);
        saveOnlyMemberReadableTopicOfCount(2);

        AuthMember user = MemberFixture.createUser(member);

        //when
        List<TopicResponse> topics = topicQueryService.findAllReadable(user);

        //then
        assertThat(topics).hasSize(3);
        assertThat(topics).extractingResultOf("name")
                .containsExactlyInAnyOrder(
                        "아무나 읽을 수 있는 토픽",
                        "토픽 멤버만 읽을 수 있는 토픽",
                        "토픽 멤버만 읽을 수 있는 토픽"
                );
    }

    @Test
    @DisplayName("읽을 수 있는 모든 Topic 목록을 조회한다.")
    void findAllReadable_Success2() {
        //given
        saveAllReadableTopicOfCount(1);
        saveOnlyMemberReadableTopicOfCount(10);

        //when
        AuthMember guest = new Guest();

        List<TopicResponse> topics = topicQueryService.findAllReadable(guest);

        //then
        assertThat(topics).hasSize(1);
        assertThat(topics).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽");
    }


    void saveAllReadableTopicOfCount(int count) {
        for (int i = 0; i < count; i++) {
            topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        }
    }

    void saveOnlyMemberReadableTopicOfCount(int count) {
        for (int i = 0; i < count; i++) {
            topicRepository.save(TopicFixture.createPrivateAndGroupOnlyTopic(member));
        }
    }

    @Test
    @DisplayName("권한이 있는 토픽을 ID로 조회한다.")
    void findDetailById_Success() {
        //given
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic);

        //when
        AuthMember guest = new Guest();

        TopicDetailResponse detail = topicQueryService.findDetailById(guest, topic.getId());

        //then
        assertThat(detail.id()).isEqualTo(topic.getId());
        assertThat(detail.name()).isEqualTo("아무나 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 없는 토픽을 ID로 조회하면, 예외가 발생한다.")
    void findDetailById_Fail() {
        //given
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(member);

        topicRepository.save(topic);

        //when then
        AuthMember guest = new Guest();

        assertThatThrownBy(() -> topicQueryService.findDetailById(guest, topic.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조회권한이 없는 Topic 입니다.");
    }

    @Test
    @DisplayName("권한이 있는 토픽을 여러개의 ID로 조회한다.")
    void findDetailByIds_Success1() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember guest = new Guest();

        List<TopicDetailResponse> details = topicQueryService.findDetailsByIds(
                guest,
                List.of(topic1.getId(), topic2.getId())
        );

        //then
        assertThat(details).hasSize(2);
        assertThat(details).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽", "아무나 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 있는 토픽을 여러개의 ID로 조회한다.")
    void findDetailByIds_Success2() {
        //given
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when
        AuthMember user = MemberFixture.createUser(member);

        List<TopicDetailResponse> details = topicQueryService.findDetailsByIds(
                user,
                List.of(topic1.getId(), topic2.getId())
        );

        //then
        assertThat(details).hasSize(2);
        assertThat(details).extractingResultOf("name")
                .containsExactlyInAnyOrder("아무나 읽을 수 있는 토픽", "토픽 멤버만 읽을 수 있는 토픽");
    }

    @Test
    @DisplayName("권한이 없는 토픽을 여러개의 ID로 조회하면, 예외가 발생한다.")
    void findDetailByIds_Fail1() {
        //given
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when then
        AuthMember guest = new Guest();
        List<Long> topicIds = List.of(topic1.getId(), topic2.getId());

        assertThatThrownBy(() -> topicQueryService.findDetailsByIds(guest, topicIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("읽을 수 없는 토픽이 존재합니다.");
    }

    @Test
    @DisplayName("조회하려는 토픽 중 존재하지 않는 ID가 존재하면, 예외가 발생한다.")
    void findDetailByIds_Fail2() {
        //given
        Topic topic1 = TopicFixture.createPublicAndAllMembersTopic(member);
        Topic topic2 = TopicFixture.createPublicAndAllMembersTopic(member);

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        //when then
        AuthMember user = MemberFixture.createUser(member);
        List<Long> topicIds = List.of(topic1.getId(), topic2.getId(), Long.MAX_VALUE);

        assertThatThrownBy(() -> topicQueryService.findDetailsByIds(user, topicIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 토픽이 존재합니다");
    }

    @Test
    @DisplayName("핀 수정일 기준으로 토픽을 나열한다")
    void findAllByOrderByUpdatedAtDesc_Success() {
        // given
        Location location = LocationFixture.create();
        locationRepository.save(location);

        List<Topic> topics = List.of(
                TopicFixture.createByName("5등", member),
                TopicFixture.createByName("4등", member),
                TopicFixture.createByName("3등", member),
                TopicFixture.createByName("2등", member),
                TopicFixture.createByName("1등", member)

        );
        topicRepository.saveAll(topics);

        List<Pin> pins = topics.stream()
                .map(topic -> PinFixture.create(location, topic, member))
                .toList();
        pinRepository.saveAll(pins);

        // when
        List<TopicResponse> responses = topicQueryService.findAllByOrderByUpdatedAtDesc(new Guest());

        // then
        assertThat(responses).isNotEmpty();
    }
}
