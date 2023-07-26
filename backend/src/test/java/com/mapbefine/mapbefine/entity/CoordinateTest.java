package com.mapbefine.mapbefine.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CoordinateTest {

    @ParameterizedTest
    @ValueSource(strings = {"32.9", "43.1"})
    @DisplayName("위도의 값이 33~43사이의 값이 아니면 실패한다.")
    void createCoordinate_FailByInvalidLatitude(String input) {
        BigDecimal latitude = new BigDecimal(input);

        assertThatThrownBy(() -> new Coordinate(latitude, BigDecimal.valueOf(127)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한국 내의 좌표만 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"33.1", "42.9"})
    @DisplayName("위도의 값이 33~43사이의 값이면 통과한다.")
    void createLatitude_Success(String input) {
        BigDecimal latitude = new BigDecimal(input);

        assertDoesNotThrow(() -> new Coordinate(latitude, BigDecimal.valueOf(127)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"124.1", "131.9"})
    @DisplayName("경도의 값이 124~132사이의 값이면 통과한다.")
    void createLongitude_Success(String input) {
        BigDecimal longitude = new BigDecimal(input);

        assertDoesNotThrow(() -> new Coordinate(BigDecimal.valueOf(37), longitude));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.9", "132.1"})
    @DisplayName("경도의 값이 124~132사이의 값이 아니면 실패한다.")
    void createCoordinate_FailByInvalidLongitude(String input) {
        BigDecimal longitude = new BigDecimal(input);

        assertThatThrownBy(() -> new Coordinate(BigDecimal.valueOf(37), longitude))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한국 내의 좌표만 입력해주세요.");
    }

    @ParameterizedTest
    @MethodSource(value = "calculateDistanceProvider")
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance(Coordinate coordinate, Coordinate other, BigDecimal underBound, BigDecimal upperBound) {
        // when
        BigDecimal result = coordinate.calculateDistance(other);

        // then
        assertThat(result).isBetween(underBound, upperBound);
    }

    static Stream<Arguments> calculateDistanceProvider() {
        return Stream.of(
                Arguments.of(
                        new Coordinate(BigDecimal.valueOf(37.6273677), BigDecimal.valueOf(127.0447364)),
                        new Coordinate(BigDecimal.valueOf(37.6273438), BigDecimal.valueOf(127.0447853)),
                        BigDecimal.valueOf(500 * 0.98),
                        BigDecimal.valueOf(500 * 1.02)
                ),
                Arguments.of(
                        new Coordinate(BigDecimal.valueOf(37.5909374), BigDecimal.valueOf(127.1537482)),
                        new Coordinate(BigDecimal.valueOf(37.610454), BigDecimal.valueOf(127.2050749)),
                        BigDecimal.valueOf(500000 * 0.98),
                        BigDecimal.valueOf(500000 * 1.02)
                ),
                Arguments.of(
                        new Coordinate(BigDecimal.valueOf(37.6273677), BigDecimal.valueOf(127.0447364)),
                        new Coordinate(BigDecimal.valueOf(37.6273777), BigDecimal.valueOf(127.0447364)),
                        BigDecimal.valueOf(111 * 0.98),
                        BigDecimal.valueOf(111 * 1.02))
        );
    }

}
