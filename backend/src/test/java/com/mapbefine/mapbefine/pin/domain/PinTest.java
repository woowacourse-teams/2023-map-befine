package com.mapbefine.mapbefine.pin.domain;

import static com.mapbefine.mapbefine.pin.domain.Pin.createPinAssociatedWithLocationAndTopic;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PinTest {

    private static final Location LOCATION = LocationFixture.create();
    private static final Topic TOPIC = TopicFixture.createByName("topic", MemberFixture.create(Role.USER));
    private static final PinInfo VALID_PIN_INFO = PinInfo.of("name", "description");

    @Test
    @DisplayName("올바른 핀 정보를 주면 성공적으로 Pin 을 생성한다.")
    void createPinAssociatedWithLocationAndTopic_Success() {
        // given
        Pin pin = createPinAssociatedWithLocationAndTopic(VALID_PIN_INFO, LOCATION, TOPIC);

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
        Pin pin = createPinAssociatedWithLocationAndTopic(
                PinInfo.of("before name", "before description"),
                LOCATION,
                TOPIC
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
