package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class CoordinateTest {

    @ParameterizedTest
    @ValueSource(doubles = {32.9, 43.1})
    @DisplayName("위도의 값이 33~43사이의 값이 아니면 실패한다.")
    void createCoordinate_FailByInvalidLatitude(double latitude) {
        assertThatThrownBy(() -> Coordinate.of(latitude, 127))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한국 내의 좌표만 입력해주세요.");
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한국 내의 좌표만 입력해주세요.");
    }

    @ParameterizedTest
    @MethodSource(value = "calculateDistanceProvider")
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance(Coordinate coordinate, Coordinate other, double underBound, double upperBound) {
        // when
        double result = coordinate.calculateDistance(other);

        // then
        assertThat(result).isBetween(underBound, upperBound);
    }

    static Stream<Arguments> calculateDistanceProvider() {
        return Stream.of(
                Arguments.of(
                        Coordinate.of(37.6273677, 127.0447364),
                        Coordinate.of(37.6273438, 127.0447853),
                        500 * 0.98,
                        500 * 1.02
                ),
                Arguments.of(
                        Coordinate.of(37.5909374, 127.1537482),
                        Coordinate.of(37.610454, 127.2050749),
                        500000 * 0.98,
                        500000 * 1.02
                ),
                Arguments.of(
                        Coordinate.of(37.6273677, 127.0447364),
                        Coordinate.of(37.6273777, 127.0447364),
                        111 * 0.98,
                        111 * 1.02
                )
        );
    }

}
