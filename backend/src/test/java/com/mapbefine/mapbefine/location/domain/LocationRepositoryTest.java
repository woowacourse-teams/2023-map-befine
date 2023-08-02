package com.mapbefine.mapbefine.location.domain;

import com.mapbefine.mapbefine.location.LocationFixture;
import java.util.List;
import java.util.stream.Collectors;
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

    @BeforeEach
    void setUp() {
        List<Location> locations = LocationFixture.COORDINATES.stream()
                .map(LocationFixture::from)
                .collect(Collectors.toList());

        locationRepository.saveAll(locations);
    }

    @ParameterizedTest
    @MethodSource(value = "coordinates_Fail")
    @DisplayName("범위 내의 Pin을 검색한다")
    void findAllByCoordinateAndDistanceInMeters(double distance, int size) {
        //when
        List<Location> locations = locationRepository.findAllByCoordinateAndDistanceInMeters(
                LocationFixture.BASE_COORDINATE, distance);

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
