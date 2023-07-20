package com.mapbefine.mapbefine.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CoordinateTest {

    @ParameterizedTest
    @ValueSource(strings = {"32.9", "43.1"})
    @DisplayName("위도의 값이 33~43사이의 값이 아니면 실패한다.")
    void validateLatitude_Fail(String input) {
        BigDecimal latitude = new BigDecimal(input);

        assertThatThrownBy(() -> new Coordinate(latitude, BigDecimal.valueOf(127)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한국 내의 좌표만 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"33.1", "42.9"})
    @DisplayName("위도의 값이 33~43사이의 값이면 통과한다.")
    void validateLatitude_Success(String input) {
        BigDecimal latitude = new BigDecimal(input);

        assertDoesNotThrow(() -> new Coordinate(latitude, BigDecimal.valueOf(127)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"124.1", "131.9"})
    @DisplayName("경도의 값이 124~132사이의 값이면 통과한다.")
    void validateLongitude_Success(String input) {
        BigDecimal longitude = new BigDecimal(input);

        assertDoesNotThrow(() -> new Coordinate(BigDecimal.valueOf(37), longitude));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.9", "132.1"})
    @DisplayName("경도의 값이 124~132사이의 값이 아니면 실패한다.")
    void validateLongitude_Fail(String input) {
        BigDecimal longitude = new BigDecimal(input);

        assertThatThrownBy(() -> new Coordinate(BigDecimal.valueOf(37), longitude))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한국 내의 좌표만 입력해주세요.");
    }

    @Test
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance1() {
        // given
        Coordinate coordinate = new Coordinate(BigDecimal.valueOf(37.6273677), BigDecimal.valueOf(127.0447364));
        Coordinate otherCoordinate = new Coordinate(BigDecimal.valueOf(37.6273438), BigDecimal.valueOf(127.0447853));

        // when
        BigDecimal result = coordinate.calculateDistance(otherCoordinate);

        // then
        assertThat(result).isBetween(BigDecimal.valueOf(500 * 0.98), BigDecimal.valueOf(500 * 1.02));
    }

    @Test
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance2() {
        // given
        Coordinate coordinate = new Coordinate(BigDecimal.valueOf(37.5909374), BigDecimal.valueOf(127.1537482));
        Coordinate otherCoordinate = new Coordinate(BigDecimal.valueOf(37.610454), BigDecimal.valueOf(127.2050749));

        // when
        BigDecimal result = coordinate.calculateDistance(otherCoordinate);

        // then
        assertThat(result).isBetween(BigDecimal.valueOf(500000 * 0.98), BigDecimal.valueOf(500000 * 1.02));
    }

    @Test
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance3() {
        // given
        Coordinate coordinate = new Coordinate(BigDecimal.valueOf(37.6273677), BigDecimal.valueOf(127.0447364));
        Coordinate otherCoordinate = new Coordinate(BigDecimal.valueOf(37.6273777), BigDecimal.valueOf(127.0447364));

        // when
        BigDecimal result = coordinate.calculateDistance(otherCoordinate);

        // then
        assertThat(result).isBetween(BigDecimal.valueOf(111 * 0.98), BigDecimal.valueOf(111 * 1.02));
    }

}
