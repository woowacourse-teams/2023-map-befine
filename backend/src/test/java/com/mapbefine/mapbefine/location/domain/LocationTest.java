package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    @DisplayName("정상적인 값이 입력되면 객체가 생성된다")
    void create() {
        //given
        double expectedLatitude = 37.6273677;
        double expectedLongitude = 127.0447364;

        Location location = new Location(
                LocationFixture.ADDRESS,
                Coordinate.of(expectedLatitude, expectedLongitude)
        );

        //when
        Address actualAddress = location.getAddress();
        double actualLatitude = location.getLatitude();
        double actualLongitude = location.getLongitude();

        //then
        assertThat(actualAddress.getRoadBaseAddress()).isEqualTo(LocationFixture.ROAD_ADDRESS);
        assertThat(actualLatitude).isEqualTo(expectedLatitude);
        assertThat(actualLongitude).isEqualTo(expectedLongitude);
    }

    @Test
    @DisplayName("위치에 핀을 추가하면, 핀에도 위치가 추가된다.")
    void addPin() {
        //given
        Member member = MemberFixture.create("member", "member@naver.com", Role.ADMIN);
        Topic topic = TopicFixture.createByName("쥬니의 오락실", member);
        Location location = LocationFixture.LOCATION;

        Pin pin = PinFixture.create(location, topic, member);

        //when
        Location pinLocation = pin.getLocation();
        List<Pin> pins = location.getPins();

        //then
        assertThat(pinLocation).isEqualTo(location);
        assertThat(pins).contains(pin);
    }

    @Test
    @DisplayName("같은 주소를 입력하면 참을 반환한다")
    void isSameAddress() {
        //given
        Location location = LocationFixture.LOCATION;

        //when
        boolean sameParcelAddress = location.isSameAddress(LocationFixture.PARCEL_ADDRESS);
        boolean sameRoadAddress = location.isSameAddress(LocationFixture.ROAD_ADDRESS);

        //then
        assertThat(sameParcelAddress).isTrue();
        assertThat(sameRoadAddress).isTrue();
    }
}
