package com.mapbefine.mapbefine.location.domain;

import com.mapbefine.mapbefine.location.AddressFixture;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private Coordinate baseCoordinate;
    private Location meter;
    private Location tenMeter;
    private Location threeKiloMeter;
    private Location tenKiloMeter;


    @BeforeEach
    void setUp() {
        baseCoordinate = Coordinate.of(37.6273677, 127.0447364);
        meter = new Location(
                AddressFixture.create(),
                Coordinate.of(37.6273644, 127.0447427)
        ); // 직선 거리 약 0.669 미터
        tenMeter = new Location(
                AddressFixture.create(),
                Coordinate.of(37.6274194, 127.0447922)
        ); // 직선 거리 약 7.565 미터
        threeKiloMeter = new Location(
                AddressFixture.create(),
                Coordinate.of(37.6316286, 127.0650012)
        ); // 직선 거리 약 1846.414 미터
        tenKiloMeter = new Location(
                AddressFixture.create(),
                Coordinate.of(37.6507643, 127.1115283)
        ); // 직선 거리 약 6430.905 미터
    }


    @ParameterizedTest
    @MethodSource(value = "coordinates_Fail")
    @DisplayName("범위 내의 Pin을 검색한다")
    void findAllByCoordinateAndDistanceInMeters(double distance, int size) {
        //given
        locationRepository.saveAll(List.of(meter, tenMeter, threeKiloMeter, tenKiloMeter));

        //when
        List<Location> locations = locationRepository.findAllByCoordinateAndDistanceInMeters(
                baseCoordinate, distance);

        //then
        Assertions.assertThat(locations).hasSize(size);
    }

    static Stream<Arguments> coordinates_Fail() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(10, 2),
                Arguments.of(3_000, 3),
                Arguments.of(10_000, 4)
        );
    }

}
