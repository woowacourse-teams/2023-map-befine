package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinInfo;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PinTest {

    private static final Location location = new Location(
            new Address(
                    "parcel",
                    "road",
                    "legalDongCode"
            ),
            Coordinate.of(
                    38.123456,
                    127.123456
            )
    );
    private static final Topic topic = Topic.createTopicAssociatedWithMember(
            "topicName",
            "topicDescription",
            null,
            Publicity.PUBLIC,
            Permission.ALL_MEMBERS,
            MemberFixture.create("member", "member@naver.com", Role.ADMIN)
    );
    private static final Member member = Member.of(
            "memberr",
            "memberr@naver.com",
            "https://map-befine-official.github.io/favicon.png",
            Role.ADMIN
    );

    @ParameterizedTest
    @MethodSource(value = "validNameAndDescription")
    @DisplayName("올바른 정보를 주면 성공적으로 Pin 을 생성한다.")
    void createPinAssociatedWithLocationAndTopic_Success(String name, String description) {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                name,
                description,
                location,
                topic,
                member
        );

        PinInfo pinInfo = pin.getPinInfo();

        // when
        String actualName = pinInfo.getName();
        String actualDescription = pinInfo.getDescription();

        // then
        assertThat(actualName).isEqualTo(name);
        assertThat(actualDescription).isEqualTo(description);
    }

    @ParameterizedTest
    @MethodSource(value = "invalidNameOrDescription")
    @DisplayName("잘못된 정보를 주면 Pin 을 생성을 실패한다.")
    void createPinAssociatedWithLocationAndTopic_Fail(String name, String description) {
        // given when then
        assertThatThrownBy(() -> Pin.createPinAssociatedWithLocationAndTopicAndMember(
                name,
                description,
                location,
                topic,
                member
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource(value = "validNameAndDescription")
    @DisplayName("정상적인 정보를 통해 Update 를 진행하면 Pin 의 정보가 정상적으로 수정된다.")
    void update_Success(String name, String description) {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember("name", "description", location, topic, member);

        // when
        pin.updatePinInfo(name, description);
        PinInfo pinInfo = pin.getPinInfo();
        String actualName = pinInfo.getName();
        String actualDescription = pinInfo.getDescription();

        // then
        assertThat(actualName).isEqualTo(name);
        assertThat(actualDescription).isEqualTo(description);
    }

    static Stream<Arguments> validNameAndDescription() {
        return Stream.of(
                Arguments.of("1", "1"),
                Arguments.of("1", "1".repeat(1000)),
                Arguments.of("1".repeat(50), "1"),
                Arguments.of("1".repeat(50), "1".repeat(1000))
        );
    }

    @ParameterizedTest
    @MethodSource(value = "invalidNameOrDescription")
    @DisplayName("정상적인 정보를 통해 Update 를 진행하면 Pin 의 수정을 실패한다.")
    void update_Fail(String name, String description) {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                "name",
                "description",
                location,
                topic,
                member
        );

        // when then
        assertThatThrownBy(() -> pin.updatePinInfo(name, description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Pin 을 생성하면, Location, Topic, Member 에 등록이 된다.")
    void createPinAssociatedWithLocationAndTopicAndMember() {
        // given
        Member mockMember = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        Topic mockTopic = TopicFixture.createByName("topicName", mockMember);
        Location mockLocation = LocationFixture.create();

        // when
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                "name",
                "description",
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

    static Stream<Arguments> invalidNameOrDescription() {
        return Stream.of(
                Arguments.of(null, "1"),
                Arguments.of("1", null),
                Arguments.of(null, null),
                Arguments.of("   ", "1"),
                Arguments.of("1", "    "),
                Arguments.of("", "1"),
                Arguments.of("1", ""),
                Arguments.of("1".repeat(51), "1"),
                Arguments.of("1", "1".repeat(1001)),
                Arguments.of("1".repeat(51), "1".repeat(1001))
        );
    }

}
