package com.mapbefine.mapbefine.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberQueryServiceTest {

    @Autowired
    private MemberQueryService memberQueryService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PinRepository pinRepository;

    @Test
    @DisplayName("유저 목록을 조회한다.")
    void findAllMember() {
        // given
        Member member = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Member memberr = memberRepository.save(
                MemberFixture.create("memberr", "memberr@naver.com", Role.USER)
        );

        // when
        List<MemberResponse> responses = memberQueryService.findAll();

        // then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(List.of(MemberResponse.from(member), MemberResponse.from(memberr)));
    }

    @Test
    @DisplayName("유저를 단일 조회한다.")
    void findMemberById() {
        // given
        Member member = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );

        // when
        MemberDetailResponse response = memberQueryService.findById(member.getId());

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(MemberDetailResponse.from(member));
    }

    @Test
    @DisplayName("조회하려는 유저가 없는 경우 예외를 반환한다.")
    void findMemberById_whenNoneExists_thenFail() {
        // given when then
        assertThatThrownBy(() -> memberQueryService.findById(Long.MAX_VALUE))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("유저가 만든 핀을 조회한다.")
    void findPinsByMember() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Location location = locationRepository.save(LocationFixture.create());
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        Pin pin1 = pinRepository.save(
                PinFixture.create(
                        location,
                        topic,
                        creator
                )
        );
        Pin pin2 = pinRepository.save(
                PinFixture.create(
                        location,
                        topic,
                        creator
                )
        );
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // when
        List<PinResponse> response = memberQueryService.findPinsByMember(authCreator);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(List.of(PinResponse.from(pin1), PinResponse.from(pin2)));
    }

    @Test
    @DisplayName("존재하지 않는 유저가 Pin 을 조회할 때 예외가 발생한다.")
    void findPinsByMember_whenNoneExists_thenFail() {
        // given when then
        assertThatThrownBy(() -> memberQueryService.findPinsByMember(new Guest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유저가 만든 토픽을 조회한다.")
    void findTopicsByMember() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Topic topic1 = topicRepository.save(TopicFixture.createByName("topic1", creator));
        Topic topic2 = topicRepository.save(TopicFixture.createByName("topic2", creator));
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // when
        List<TopicResponse> response = memberQueryService.findTopicsByMember(authCreator);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(List.of(TopicResponse.from(topic1), TopicResponse.from(topic2)));
    }

    @Test
    @DisplayName("존재하지 않는 유저가 본인이 만든 토픽을 조회할 때 예외가 발생한다.")
    void findTopicsByMember_whenNoneExists_thenFail() {
        // given when then
        assertThatThrownBy(() -> memberQueryService.findTopicsByMember(new Guest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> getTopicsWithPermission(Member member) {
        return member.getTopicsWithPermissions()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    private List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopics()
                .stream()
                .map(Topic::getId)
                .toList();
    }

}
