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
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TopicCommandServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TopicCommandService topicCommandService;

    private Topic TOPIC_WITH_TWO_PINS;
    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        authMember = new Admin(member.getId());;
        TOPIC_WITH_TWO_PINS = TopicFixture.createByName("준팍의 또간집", member);

        Location location = LocationFixture.create();
        locationRepository.save(location);

        PinFixture.create(location, TOPIC_WITH_TWO_PINS);
        PinFixture.create(location, TOPIC_WITH_TWO_PINS);

        topicRepository.save(TOPIC_WITH_TWO_PINS);
    }

    @Test
    @DisplayName("Topic의 이름이나 설명을 수정하면 DB에 반영한다.")
    void update() {
        //given
        Long id = TOPIC_WITH_TWO_PINS.getId();

        //when
        String name = "준팍의 안갈 집";
        String description = "다시는 안갈 집";
        String imageUrl = "https://map-befine-official.github.io/favicon.png";
        topicCommandService.updateTopicInfo(authMember, id, new TopicUpdateRequest(name, imageUrl, description));

        //then
        Topic topic = topicRepository.findById(id).get();
        TopicInfo topicInfo = topic.getTopicInfo();
        assertThat(topicInfo.getName()).isEqualTo(name);
        assertThat(topicInfo.getDescription()).isEqualTo(description);
        assertThat(topicInfo.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("Topic을 삭제하면 토픽과 안의 Pin들이 isDeleted가 true로 변한다")
    void delete() {
        //given
        Long id = TOPIC_WITH_TWO_PINS.getId();

        //when
        topicCommandService.delete(authMember, id);

        //then
        Topic deletedTopic = topicRepository.findById(id).get();
        assertThat(deletedTopic.getId()).isEqualTo(id);
        assertThat(deletedTopic.isDeleted()).isTrue();
        for (Pin pin : deletedTopic.getPins()) {
            assertThat(pin.isDeleted()).isTrue();
        }
    }

}
