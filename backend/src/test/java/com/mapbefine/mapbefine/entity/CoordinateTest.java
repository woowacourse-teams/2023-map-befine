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
        Coordinate coordinate1 = new Coordinate(BigDecimal.valueOf(37.6273438), BigDecimal.valueOf(127.0447853));

        // when
        BigDecimal result = coordinate.calculateDistance(coordinate1);

        // then
        assertThat(result).isGreaterThan(BigDecimal.valueOf(400))
                .isLessThan(BigDecimal.valueOf(600));
    }

    @Test
    @DisplayName("좌표 사이의 거리를 계산한다.")
    void calculateDistance2() {
        // given
        Coordinate coordinate = new Coordinate(BigDecimal.valueOf(37.6273677), BigDecimal.valueOf(127.0447364));
        Coordinate coordinate1 = new Coordinate(BigDecimal.valueOf(37.6273777), BigDecimal.valueOf(127.0447364));

        // when then
        assertThat(coordinate.calculateDistance(coordinate1)).isEqualTo(BigDecimal.valueOf(100));
    }

}