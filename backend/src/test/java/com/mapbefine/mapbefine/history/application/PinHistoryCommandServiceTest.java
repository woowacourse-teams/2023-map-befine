package com.mapbefine.mapbefine.history.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.history.domain.PinHistory;
import com.mapbefine.mapbefine.history.domain.PinHistoryRepository;
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
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@ServiceTest
class PinHistoryCommandServiceTest extends TestDatabaseContainer {

    @Autowired
    private PinHistoryRepository pinHistoryRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    @DisplayName("핀 변경 이벤트가 발생하면, 핀을 수정한 사람, 핀 정보를 포함한 정보 이력을 저장한다.")
    void saveHistory_Success() {
        // given
        Member member = memberRepository.save(MemberFixture.create("핀 변경한 사람", "pinUpdateBy@gmail.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        Location location = locationRepository.save(LocationFixture.create());
        Pin pin = pinRepository.save(PinFixture.create(location, topic, member));

        // when
        applicationEventPublisher.publishEvent(new PinUpdateEvent(pin, member));

        // then
        List<PinHistory> histories = pinHistoryRepository.findAllByPinId(pin.getId());
        PinHistory expected = new PinHistory(pin, member);
        PinHistory actual = histories.get(0);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀 정보 이력에는 해당 정보가 수정된 일시를 함께 저장한다.")
    void saveHistory_Success_createdAt() {
        // given
        Member member = memberRepository.save(MemberFixture.create("핀 변경한 사람", "pinUpdateBy@gmail.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createPublicAndAllMembersTopic(member));
        Location location = locationRepository.save(LocationFixture.create());
        Pin pin = pinRepository.save(PinFixture.create(location, topic, member));

        // when
        applicationEventPublisher.publishEvent(new PinUpdateEvent(pin, member));

        // then
        List<PinHistory> histories = pinHistoryRepository.findAllByPinId(pin.getId());
        LocalDateTime expectedPinUpdatedAt = pin.getUpdatedAt();
        PinHistory actual = histories.get(0);
        LocalDateTime actualPinUpdatedAt = actual.getPinUpdatedAt();
        assertThat(expectedPinUpdatedAt).isEqualTo(actualPinUpdatedAt);
    }

}
