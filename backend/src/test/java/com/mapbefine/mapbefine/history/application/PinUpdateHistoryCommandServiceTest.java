package com.mapbefine.mapbefine.history.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.history.domain.PinUpdateHistory;
import com.mapbefine.mapbefine.history.domain.PinUpdateHistoryRepository;
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
import com.mapbefine.mapbefine.pin.event.PinBatchDeleteEvent;
import com.mapbefine.mapbefine.pin.event.PinDeleteEvent;
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@ServiceTest
class PinUpdateHistoryCommandServiceTest {

    @Autowired
    private PinUpdateHistoryRepository pinUpdateHistoryRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    private Member member;
    private Pin pin;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("핀 수정한 사람", "pinUpdateBy@gmail.com", Role.USER));
        Location location = locationRepository.save(LocationFixture.create());
        Topic topic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        pin = pinRepository.save(PinFixture.create(location, topic, member));
    }

    @Test
    @DisplayName("핀 수정 이벤트가 발생하면, 핀을 수정한 사람, 핀 정보를 포함한 수정 이력을 저장한다.")
    void saveHistory_Success() {
        // given
        // when
        applicationEventPublisher.publishEvent(new PinUpdateEvent(pin, member));

        // then
        List<PinUpdateHistory> histories = pinUpdateHistoryRepository.findAllByPinId(pin.getId());
        assertThat(histories.get(0)).usingRecursiveComparison()
                .ignoringFields("id", "updatedAt")
                .isEqualTo(new PinUpdateHistory(pin, member));
    }

    @Test
    @DisplayName("핀 삭제 이벤트가 발생하면, 핀 수정 이력을 삭제 상태로 변경한다.")
    void deleteHistory_Success() {
        // given
        applicationEventPublisher.publishEvent(new PinUpdateEvent(pin, member));

        // when
        Long pinId = pin.getId();
        applicationEventPublisher.publishEvent(new PinDeleteEvent(pinId));

        // then
        assertThat(pinUpdateHistoryRepository.findAllByPinId(pinId))
                .isEmpty();
    }

    @Test
    @DisplayName("핀 다건 삭제 이벤트가 발생하면, 핀 수정 이력을 삭제 상태로 변경한다.")
    void batchDeleteHistory_Success() {
        // given
        applicationEventPublisher.publishEvent(new PinUpdateEvent(pin, member));

        // when
        Long pinId = pin.getId();
        applicationEventPublisher.publishEvent(new PinBatchDeleteEvent(List.of(pinId)));

        // then
        assertThat(pinUpdateHistoryRepository.findAllByPinId(pinId))
                .isEmpty();
    }

}
