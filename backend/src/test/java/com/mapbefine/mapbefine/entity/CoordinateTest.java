package com.mapbefine.mapbefine.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoordinateTest {

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
    void calculateDistance3() {
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
    void calculateDistance2() {
        // given
        Coordinate coordinate = new Coordinate(BigDecimal.valueOf(37.6273677), BigDecimal.valueOf(127.0447364));
        Coordinate otherCoordinate = new Coordinate(BigDecimal.valueOf(37.6273777), BigDecimal.valueOf(127.0447364));

        // when
        BigDecimal result = coordinate.calculateDistance(otherCoordinate);

        // then
        assertThat(result).isBetween(BigDecimal.valueOf(111 * 0.98), BigDecimal.valueOf(111 * 1.02));
    }

}