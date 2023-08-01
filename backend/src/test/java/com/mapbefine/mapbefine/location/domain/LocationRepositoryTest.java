package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @ParameterizedTest
    @MethodSource(value = "coordinates_Success")
    @DisplayName("범위 내의 Pin을 검색한다.")
    void findAllByRectangle_Success(double latitude, double longitude) {
        // given
//        double distance = 0.00011;
        double distance = 0.0010;
        // 지옥의 문제가 다시 시작되었다..... 자릿수 문제를 해결하기 위해서는 어쩔 수 없이 BigDecimal 을 사용하는 방법밖에 없다.
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        Address address = new Address(
                "parcel",
                "road",
                "legalDongCode"
        );
        Location location = new Location(
                address,
                coordinate
        );
        locationRepository.save(location);


        // when
        List<Location> locations = locationRepository.findAllByRectangle(
                35,
                127,
                distance
        );

        // then
        assertThat(locations).hasSize(1);
    }

    static Stream<Arguments> coordinates_Success() {
        return Stream.of(
                Arguments.of(34.9999, 126.999),
                Arguments.of(35.0001, 126.999),
                Arguments.of(34.9999, 127.000),
                Arguments.of(35.0001, 127.000)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "coordinates_Fail")
    @DisplayName("범위 내의 Pin을 검색한다.")
    void findAllByRectangle_Fail(double latitude, double longitude) {
        // given
        double distance = 0.0001;
        Coordinate coordinate = Coordinate.of(latitude, longitude);
        Address address = new Address(
                "지번주소",
                "도로명주소",
                "111000"
        );
        Location location = new Location(
                address,
                coordinate
        );
        locationRepository.save(location);

        // when
        List<Location> locations = locationRepository.findAllByRectangle(
                35,
                127,
                distance
        );

        // then
        assertThat(locations).isEmpty();
    }

    static Stream<Arguments> coordinates_Fail() {
        return Stream.of(
                Arguments.of(34.9999, 126.999),
                Arguments.of(35.0001, 126.999),
                Arguments.of(34.9998, 127.000),
                Arguments.of(35.0002, 127.000)
        );
    }

}
