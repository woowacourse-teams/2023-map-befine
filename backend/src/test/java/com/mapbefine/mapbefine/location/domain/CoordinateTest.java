package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.exception.LocationException.LocationBadRequestException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class CoordinateTest {

    @Nested
    class Validate {

        @ParameterizedTest
        @ValueSource(doubles = {32.9, 43.1})
        @DisplayName("위도의 값이 33~43사이의 값이 아니면 실패한다.")
        void createCoordinate_FailByInvalidLatitude(double latitude) {
            assertThatThrownBy(() -> Coordinate.of(latitude, 127))
                    .isInstanceOf(LocationBadRequestException.class);
        }

        @ParameterizedTest
        @ValueSource(doubles = {33.1, 42.9})
        @DisplayName("위도의 값이 33~43사이의 값이면 통과한다.")
        void createLatitude_Success(double latitude) {
            assertDoesNotThrow(() -> Coordinate.of(latitude, 127));
        }

        @ParameterizedTest
        @ValueSource(doubles = {124.1, 131.9})
        @DisplayName("경도의 값이 124~132사이의 값이면 통과한다.")
        void createLongitude_Success(double longitude) {
            assertDoesNotThrow(() -> Coordinate.of(37, longitude));
        }

        @ParameterizedTest
        @ValueSource(doubles = {123.9, 132.1})
        @DisplayName("경도의 값이 124~132사이의 값이 아니면 실패한다.")
        void createCoordinate_FailByInvalidLongitude(double longitude) {
            assertThatThrownBy(() -> Coordinate.of(37, longitude))
                    .isInstanceOf(LocationBadRequestException.class);
        }

    }

    @ParameterizedTest
    @MethodSource(value = "calculateDistanceProvider")
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance(Coordinate coordinate, Coordinate other, long expected) {
        // when
        double result = coordinate.calculateDistanceInMeters(other);

        // then
        assertThat(Math.floor(result)).isEqualTo(expected);
    }

    static Stream<Arguments> calculateDistanceProvider() {
        return Stream.of(
                Arguments.of(
                        LocationFixture.BASE_COORDINATE,
                        LocationFixture.SEVEN_METER,
                        7
                ),
                Arguments.of(
                        LocationFixture.BASE_COORDINATE,
                        LocationFixture.EIGHTEEN_FORTY_SIX_METER,
                        1846
                ),
                Arguments.of(
                        LocationFixture.BASE_COORDINATE,
                        LocationFixture.SIXTY_FOUR_THIRTY_METER,
                        6430
                )
        );
    }
}
