package com.mapbefine.mapbefine.dto;

import java.math.BigDecimal;

public record PinCreateRequest(
        Long topicId,
        String name,
        String description,
        String address,
        String legalDongCode,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
