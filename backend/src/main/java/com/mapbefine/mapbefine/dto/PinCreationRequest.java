package com.mapbefine.mapbefine.dto;

import java.math.BigDecimal;

public record PinCreationRequest(
        Long topicId,
        String name,
        String description,
        String address,
        String legalDongCode,
        BigDecimal longitude,
        BigDecimal latitude
) { }
