package com.mapbefine.mapbefine.location.application;

import static com.mapbefine.mapbefine.location.LocationFixture.ADDRESS;
import static com.mapbefine.mapbefine.location.LocationFixture.BASE_COORDINATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Coordinate;
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
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class LocationQueryServiceTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationQueryService locationQueryService;

    private Member member;
    private AuthMember authMember;
    private Location allPinsLocation;
    private List<Topic> topics;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        authMember = new Admin(member.getId());

        allPinsLocation = LocationFixture.create(ADDRESS, BASE_COORDINATE);
        locationRepository.save(allPinsLocation);

        topics = List.of(
                createAndSaveTopic("준팍의 또간집", 2),
                createAndSaveTopic("도이의 또간집", 3),
                createAndSaveTopic("패트릭의 또간집", 4)
        );

    }

    private Topic createAndSaveTopic(String topicName, int pinCounts) {
        Topic topic = TopicFixture.createByName(topicName, member);
        for (int i = 0; i < pinCounts; i++) {
            PinFixture.create(allPinsLocation, topic, member);
        }

        return topicRepository.save(topic);
    }

    @Test
    @DisplayName("주어진 좌표의 3KM 이내 Topic들을 Pin 개수의 내림차순으로 정렬하여 조회한다.")
    void findNearbyTopicsSortedByPinCount_Success() {
        // given
        Coordinate baseCoordinate = BASE_COORDINATE;

        // when
        List<TopicResponse> currentTopics = locationQueryService.findNearbyTopicsSortedByPinCount(
                authMember,
                baseCoordinate.getLatitude(),
                baseCoordinate.getLongitude()
        );

        // then
        List<TopicResponse> expected = topics.stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Topic::countPins)))
                .map(topic -> TopicResponse.from(topic, false, false))
                .collect(Collectors.toList());

        assertThat(currentTopics).isEqualTo(expected);
    }

    @Test
    @DisplayName("반경 내 핀을 찾을 때, soft delete 된 핀은 제외한다.")
    void findNearbyTopicsSortedByPinCount_Success_notContainingSoftDeletedPins() {
        // given
        Coordinate baseCoordinate = BASE_COORDINATE;
        Topic second = topics.get(1);
        deletePins(second, 2);

        // when
        List<TopicResponse> currentTopics = locationQueryService.findNearbyTopicsSortedByPinCount(
                authMember,
                baseCoordinate.getLatitude(),
                baseCoordinate.getLongitude()
        );

        // then
        assertThat(currentTopics)
                .extracting("id")
                .containsExactly(topics.get(2).getId(), topics.get(0).getId(), topics.get(1).getId());
    }

    private void deletePins(Topic topic, int deleteCounts) {
        /// TODO: 2023/10/05 Topic의 pinCount를 줄이는 로직을 삭제 로직과 통합하지 못해 테스트에서 세부 구현이 드러나는 문제가 있음
        for (int i = 0; i < deleteCounts; i++) {
            Pin delete = topic.getPins().get(i);
            delete.decrementTopicPinCount();
            pinRepository.deleteById(delete.getId());
        }
    }

}
