package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.annotation.RepositoryTest;
import com.mapbefine.mapbefine.common.config.JpaConfig;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@RepositoryTest
class PinRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Location location;
    private Topic topic;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
        location = locationRepository.save(LocationFixture.create());
    }

    @Test
    @DisplayName("핀을 삭제하면 soft-deleting 된다.")
    void deleteById_Success() {
        // given
        Pin pin = PinFixture.create(location, topic, member);
        pinRepository.save(pin);
        Long pinId = pin.getId();

        // when
        assertThat(pin.isDeleted()).isFalse();
        pinRepository.deleteById(pinId);

        // then
        assertThat(pinRepository.existsById(pinId));
    }

    @Test
    @DisplayName("토픽 ID로 핀을 삭제하면 soft-deleting 된다.")
    void deleteAllByTopicId_Success() {
        // given
        for (int i = 0; i < 10; i++) {
            pinRepository.save(PinFixture.create(location, topic, member));
        }

        // when
        assertThat(topic.getPins()).extractingResultOf("isDeleted")
                .containsOnly(false);
        Long topicId = topic.getId();
        pinRepository.deleteAllByTopicId(topicId);

        // then
        assertThat(pinRepository.findAllByTopicId(topicId).isEmpty());
    }


    @Test
    @DisplayName("Member ID로 모든 핀을 soft-delete 할 수 있다.")
    void deleteAllByMemberId_Success() {
        //given
        for (int i = 0; i < 10; i++) {
            pinRepository.save(PinFixture.create(location, topic, member));
        }

        //when
        assertThat(member.getCreatedPins()).hasSize(10)
                .extractingResultOf("isDeleted")
                .containsOnly(false);
        Long memberId = member.getId();
        pinRepository.deleteAllByMemberId(memberId);

        //then
        assertThat(pinRepository.findAllByCreatorId(memberId)).isEmpty();
    }

    @Test
    @DisplayName("다른 토픽에 존재하는 핀들이여도, Member ID로 모든 핀을 soft-delete 할 수 있다.")
    void deleteAllByMemberIdInOtherTopics_Success() {
        // given
        Topic otherTopic = TopicFixture.createByName("otherTopic", member);
        topicRepository.save(otherTopic);

        for (int i = 0; i < 10; i++) {
            pinRepository.save(PinFixture.create(location, topic, member));
            pinRepository.save(PinFixture.create(location, otherTopic, member));
        }

        // when
        assertThat(member.getCreatedPins()).hasSize(20)
                .extractingResultOf("isDeleted")
                .containsOnly(false);
        Long MemberId = member.getId();
        pinRepository.deleteAllByMemberId(MemberId);

        // then
        assertThat(pinRepository.findAllByCreatorId(MemberId)).isEmpty();
    }

}
