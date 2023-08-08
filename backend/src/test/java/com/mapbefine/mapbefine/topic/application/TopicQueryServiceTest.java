package com.mapbefine.mapbefine.topic.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
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

@ServiceTest
class TopicQueryServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicQueryService topicQueryService;

    @Autowired
    private TopicRepository topicRepository;

    private Topic topic;
    private Location ALL_PINS_LOCATION;
    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        authMember = new Admin(member.getId());
        ;
        ALL_PINS_LOCATION = LocationFixture.createByCoordinate(35.0, 127.0);
        locationRepository.save(ALL_PINS_LOCATION);

        topic = createAndSaveByNameAndPinCounts("준팍의 또간집", 1);
    }

    private Topic createAndSaveByNameAndPinCounts(String topicName, int pinCounts) {
        Topic topic = TopicFixture.createByName(topicName, member);
        for (int i = 0; i < pinCounts; i++) {
            PinFixture.create(ALL_PINS_LOCATION, topic);
        }
        return topicRepository.save(topic);
    }

    @Test
    @DisplayName("모든 Topic 목록을 조회한다.")
    void findAll() {
        // given
        // when
        List<TopicResponse> topics = topicQueryService.findAll(authMember);

        // then
        assertThat(topics).contains(TopicResponse.from(topic));
    }

    @Test
    @DisplayName("해당 ID를 가진 Topic을 조회한다.")
    void findById() {
        // given
        // when
        TopicDetailResponse actual = topicQueryService.findById(authMember, topic.getId());

        // then
        assertThat(actual).isEqualTo(TopicDetailResponse.from(topic));
    }

}
