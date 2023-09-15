package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
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
        pinRepository.findById(pinId)
                .ifPresentOrElse(
                        found -> assertThat(found.isDeleted()).isTrue(),
                        Assertions::fail
                );
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
        pinRepository.deleteAllByTopicId(topic.getId());

        // then
        List<Pin> deletedPins = pinRepository.findAllByTopicId(topic.getId());
        assertThat(deletedPins).extractingResultOf("isDeleted")
                .containsOnly(true);
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
        pinRepository.deleteAllByMemberId(member.getId());

        //then
        List<Pin> deletedPins = pinRepository.findAllByCreatorId(member.getId());
        assertThat(deletedPins).extractingResultOf("isDeleted")
                .containsOnly(true);
    }

    @Test
    @DisplayName("다른 토픽에 존재하는 핀들이여도, Member ID로 모든 핀을 soft-delete 할 수 있다.")
    void deleteAllByMemberIdInOtherTopics_Success() {
        //given
        Topic otherTopic = TopicFixture.createByName("otherTopic", member);
        topicRepository.save(otherTopic);

        for (int i = 0; i < 10; i++) {
            pinRepository.save(PinFixture.create(location, topic, member));
            pinRepository.save(PinFixture.create(location, otherTopic, member));
        }

        //when
        assertThat(member.getCreatedPins()).hasSize(20)
                .extractingResultOf("isDeleted")
                .containsOnly(false);
        pinRepository.deleteAllByMemberId(member.getId());

        //then
        List<Pin> deletedPins = pinRepository.findAllByCreatorId(member.getId());
        assertThat(deletedPins).extractingResultOf("isDeleted")
                .containsOnly(true);
    }

}
