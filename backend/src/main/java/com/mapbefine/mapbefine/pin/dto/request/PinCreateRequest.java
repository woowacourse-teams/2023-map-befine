package com.mapbefine.mapbefine.pin.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record PinCreateRequest(
        Long topicId,
        String name,
        String description,
        String address,
        String legalDongCode,
        double latitude,
        double longitude,
        List<String> images
) {
}
