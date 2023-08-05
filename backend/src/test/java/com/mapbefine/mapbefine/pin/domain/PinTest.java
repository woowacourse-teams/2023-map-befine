package com.mapbefine.mapbefine.pin.domain;

import static com.mapbefine.mapbefine.pin.domain.Pin.createPinAssociatedWithLocationAndTopic;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
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
    private static final Topic topic = Topic.of(
            "topicName",
            "topicDescription",
            null,
            Publicity.PUBLIC,
            Permission.ALL_MEMBERS,
            MemberFixture.create(Role.ADMIN)
    );

    @ParameterizedTest
    @MethodSource(value = "validNameAndDescription")
    @DisplayName("올바른 핀 정보(이름, 설명)을 주면 성공적으로 Pin 을 생성한다.")
    void createPinAssociatedWithLocationAndTopic_Success(String name, String description) {
        // given
        PinInfo expected = PinInfo.of(name, description);
        Pin pin = createPinAssociatedWithLocationAndTopic(expected, location, topic);

        // when
        PinInfo actual = pin.getPinInfo();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource(value = "validNameAndDescription")
    @DisplayName("올바른 핀 정보(이름, 설명)을 주면 성공적으로 Pin 을 수정한다.")
    void update_Success(String name, String description) {
        // given
        Pin pin = createPinAssociatedWithLocationAndTopic(
                PinInfo.of("before name", "before description"),
                location,
                topic
        );

        // when
        pin.updatePinInfo(name, description);
        PinInfo actual = pin.getPinInfo();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(PinInfo.of(name, description));
    }

    private static Stream<Arguments> validNameAndDescription() {
        return Stream.of(
                Arguments.of("1", "1"),
                Arguments.of("1", "1".repeat(1000)),
                Arguments.of("1".repeat(50), "1"),
                Arguments.of("1".repeat(50), "1".repeat(1000))
        );
    }
}
