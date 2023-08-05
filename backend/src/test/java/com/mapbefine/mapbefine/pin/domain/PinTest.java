package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PinTest {


    private static final PinInfo VALID_PIN_INFO = PinInfo.of("name", "description");

    private Location location;
    private Topic topic;
    private Member member;

    @BeforeEach
    void setUp() {
        location = LocationFixture.create();
        topic = TopicFixture.createByName(
                "topic",
                MemberFixture.create("member", "member@naver.com", Role.ADMIN)
        );
        member = MemberFixture.create("pin member", "member@naver.com", Role.ADMIN);
    }

    @Test
    @DisplayName("올바른 핀 정보를 주면 성공적으로 Pin 을 생성한다.")
    void createPinAssociatedWithLocationAndTopicAndMember_validate_Success() {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                VALID_PIN_INFO,
                location,
                topic,
                member
        );

        // when
        PinInfo actual = pin.getPinInfo();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(VALID_PIN_INFO);
    }

    @Test
    @DisplayName("Pin 을 생성하면, Location, Topic, Member 에 연관 관계가 등록된다.")
    void createPinAssociatedWithLocationAndTopicAndMember_associate_Success() {
        // given, when
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                PinInfo.of("name", "description"),
                location,
                topic,
                member
        );

        List<Pin> pinsInLocation = location.getPins();
        List<Pin> pinsInTopic = topic.getPins();
        List<Pin> pinsInMember = member.getCreatedPins();

        // then
        assertThat(pinsInLocation).hasSize(1);
        assertThat(pinsInTopic).hasSize(1);
        assertThat(pinsInMember).hasSize(1);
        assertThat(pinsInLocation.get(0)).usingRecursiveComparison()
                .isEqualTo(pin);
        assertThat(pinsInTopic.get(0)).usingRecursiveComparison()
                .isEqualTo(pin);
        assertThat(pinsInMember.get(0)).usingRecursiveComparison()
                .isEqualTo(pin);
    }

    @Test
    @DisplayName("올바른 핀 정보를 주면 성공적으로 Pin 을 수정한다.")
    void updatePinInfo_Success() {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                PinInfo.of("before name", "before description"),
                location,
                topic,
                member
        );
        String updateName = VALID_PIN_INFO.getName();
        String updateDescription = VALID_PIN_INFO.getDescription();

        // when
        pin.updatePinInfo(updateName, updateDescription);
        PinInfo actual = pin.getPinInfo();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(PinInfo.of(updateName, updateDescription));
    }
}
