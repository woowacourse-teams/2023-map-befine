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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PinTest {

    private static final Location LOCATION = LocationFixture.create();
    private static final Topic TOPIC = TopicFixture.createByName(
            "topic",
            MemberFixture.create("member", "member@naver.com", Role.ADMIN)
    );
    private static final Member MEMBER = Member.of(
            "memberr",
            "memberr@naver.com",
            "https://map-befine-official.github.io/favicon.png",
            Role.ADMIN
    );
    private static final PinInfo VALID_PIN_INFO = PinInfo.of("name", "description");

    @Test
    @DisplayName("올바른 핀 정보를 주면 성공적으로 Pin 을 생성한다.")
    void createPinAssociatedWithLocationAndTopic_Success() {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                VALID_PIN_INFO,
                LOCATION,
                TOPIC,
                MEMBER
        );

        // when
        PinInfo actual = pin.getPinInfo();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(VALID_PIN_INFO);
    }

    @Test
    @DisplayName("올바른 핀 정보를 주면 성공적으로 Pin 을 수정한다.")
    void update_Success() {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                PinInfo.of("before name", "before description"),
                LOCATION,
                TOPIC,
                MEMBER
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

    @Test
    @DisplayName("Pin 을 생성하면, Location, Topic, Member 에 등록이 된다.")
    void createPinAssociatedWithLocationAndTopicAndMember_Success() {
        // given
        Member mockMember = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        Topic mockTopic = TopicFixture.createByName("topicName", mockMember);
        Location mockLocation = LocationFixture.create();

        // when
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                PinInfo.of("name", "description"),
                mockLocation,
                mockTopic,
                mockMember
        );

        List<Pin> pinsInLocation = mockLocation.getPins();
        List<Pin> pinsInTopic = mockTopic.getPins();
        List<Pin> pinsInMember = mockMember.getCreatedPins();

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
}
