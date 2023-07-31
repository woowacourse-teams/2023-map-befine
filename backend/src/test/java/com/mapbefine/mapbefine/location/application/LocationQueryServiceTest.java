package com.mapbefine.mapbefine.location.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.location.dto.CoordinateRequest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class LocationQueryServiceTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationQueryService locationQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Topic TOPIC_BEST_3RD;
    private Topic TOPIC_BEST_2ND;
    private Topic TOPIC_BEST_1ST;
    private Location ALL_PINS_LOCATION;

    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        authMember = AuthMember.from(member);
        ALL_PINS_LOCATION = LocationFixture.createByCoordinate(35.0, 127.0);
        locationRepository.save(ALL_PINS_LOCATION);

        TOPIC_BEST_3RD = createAndSaveByNameAndPinCounts("준팍의 또간집", 1);
        TOPIC_BEST_2ND = createAndSaveByNameAndPinCounts("도이의 또간집", 2);
        TOPIC_BEST_1ST = createAndSaveByNameAndPinCounts("패트릭의 또간집", 3);
    }

    private Topic createAndSaveByNameAndPinCounts(String topicName, int pinCounts) {
        Topic topic = TopicFixture.createByName(topicName, member);
        for (int i = 0; i < pinCounts; i++) {
            PinFixture.create(ALL_PINS_LOCATION, topic);
        }
        return topicRepository.save(topic);
    }

    @Test
    @DisplayName("주어진 좌표 3KM 이내의 Topic들을 Pin 개수 순서대로 조회한다.")
    void findBests() {
        // given

        CoordinateRequest currentLocation = new CoordinateRequest(
                ALL_PINS_LOCATION.getLatitude(),
                ALL_PINS_LOCATION.getLongitude()
        );

        // when
        List<TopicResponse> currentTopics = locationQueryService.findBests(authMember, currentLocation);

        // then
        assertThat(currentTopics.get(0)).isEqualTo(TopicResponse.from(TOPIC_BEST_1ST));
        assertThat(currentTopics.get(1)).isEqualTo(TopicResponse.from(TOPIC_BEST_2ND));
        assertThat(currentTopics.get(2)).isEqualTo(TopicResponse.from(TOPIC_BEST_3RD));
    }
}