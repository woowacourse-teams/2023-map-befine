package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.AddressFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocationTest {

    private Address address;
    private Coordinate coordinate;
    private Location location;

    @BeforeEach
    void setUp() {
        address = AddressFixture.create();
        coordinate = Coordinate.of(37.6273677, 127.0447364);
        location = new Location(address, coordinate);
    }

    @Test
    @DisplayName("정상적인 값이 입력되면 객체가 생성된다")
    void create() {
        //given
        double expectedLatitude = 37.6273677;
        double expectedLongitude = 127.0447364;

        Location location = Location.of(
                AddressFixture.지번_주소,
                AddressFixture.도로명_주소,
                "1100000",
                expectedLatitude,
                expectedLongitude
        );

        //when
        Address actualAddress = location.getAddress();
        double actualLatitude = location.getLatitude();
        double actualLongitude = location.getLongitude();

        //then
        assertThat(actualAddress.getRoadBaseAddress()).isEqualTo(AddressFixture.도로명_주소);
        assertThat(actualLatitude).isEqualTo(expectedLatitude);
        assertThat(actualLongitude).isEqualTo(expectedLongitude);
    }

    @Test
    @DisplayName("위치에 핀을 추가하면, 핀에도 위치가 추가된다.")
    void addPin() {
        //given
        Member member = MemberFixture.create(Role.USER);
        Topic 쥬니의_오락실 = TopicFixture.createByName("쥬니의 오락실", member);

        Pin pin = PinFixture.create(location, 쥬니의_오락실);

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
        String 지번주소 = AddressFixture.지번_주소;
        String 도로명주소 = AddressFixture.도로명_주소;

        //when
        boolean sameParcelAddress = location.isSameAddress(지번주소);
        boolean sameRoadAddress = location.isSameAddress(도로명주소);

        //then
        assertThat(sameParcelAddress).isTrue();
        assertThat(sameRoadAddress).isTrue();
    }
}
