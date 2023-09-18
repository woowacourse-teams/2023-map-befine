package com.mapbefine.mapbefine.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class LatencyLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LatencyLoggingFilter.class);

    private final LatencyRecorder latencyRecorder;
    private final QueryInspector queryInspector;

    public LatencyLoggingFilter(LatencyRecorder latencyRecorder, QueryInspector queryInspector) {
        this.latencyRecorder = latencyRecorder;
        this.queryInspector = queryInspector;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        latencyRecorder.start();

        filterChain.doFilter(request, response);

        double latencyForSeconds = latencyRecorder.getLatencyForSeconds();
        int queryCount = queryInspector.getQueryCount();
        String requestURI = request.getRequestURI();

        log.info("Latency : {}s, Query count : {}, Request URI : {}", latencyForSeconds, queryCount, requestURI);
        MDC.clear();
    }

}
