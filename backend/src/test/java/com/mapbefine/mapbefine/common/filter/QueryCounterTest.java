package com.mapbefine.mapbefine.common.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryCounterTest {

    @Test
    @DisplayName("값을 증가시킬 수 있다.")
    void increase_Success() {
        //given
        QueryCounter queryCounter = new QueryCounter();
        assertThat(queryCounter.getCount()).isZero();

        //when
        queryCounter.increase();

        //then
        assertThat(queryCounter.getCount()).isOne();
    }
}