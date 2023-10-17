package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.mapbefine.mapbefine.location.exception.LocationException.LocationBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
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

}
