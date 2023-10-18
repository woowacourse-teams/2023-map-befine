package com.mapbefine.mapbefine.pin.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PinBatchCommandService {

    private final JdbcTemplate jdbcTemplate;

    public PinBatchCommandService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    
}
