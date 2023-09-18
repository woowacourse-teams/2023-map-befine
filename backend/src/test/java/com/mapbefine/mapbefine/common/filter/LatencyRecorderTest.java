package com.mapbefine.mapbefine.common.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LatencyRecorderTest {

    private final LatencyRecorder latencyRecorder = new LatencyRecorder();

    @Test
    @DisplayName("실행 시간을 초단위로 측정할 수 있다.")
    void getLatencyForSeconds() throws InterruptedException {
        //given
        latencyRecorder.start();

        //5초
        Thread.sleep(5000);

        //when
        double latencyForSeconds = latencyRecorder.getLatencyForSeconds();

        //then
        assertThat(latencyForSeconds).isGreaterThan(5)
                .isLessThan(6);
    }

    @Test
    @DisplayName("다른 쓰레드에 영향을 받지 않는다.")
    void getLatencyForSecondsThreadSafe() throws InterruptedException {
        //given
        Thread thread = new Thread(latencyRecorder::start);
        thread.start();
        Thread.sleep(3000);

        latencyRecorder.start();
        Thread.sleep(3000);

        //when
        double latencyForSeconds = latencyRecorder.getLatencyForSeconds();

        //then
        assertThat(latencyForSeconds).isGreaterThan(3)
                .isLessThan(4);
    }

}