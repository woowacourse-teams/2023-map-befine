package com.mapbefine.mapbefine.common.filter;

import org.springframework.stereotype.Component;

@Component
public class LatencyRecorder {

    private final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public void start() {
        threadLocal.set(System.currentTimeMillis());
    }

    public double getLatencyForSeconds() {
        long start = threadLocal.get();
        long end = System.currentTimeMillis();

        threadLocal.remove();

        return (end - start) / 1000d;
    }

}
