package com.mapbefine.mapbefine.dto;

import java.util.List;

public record PinCreateRequest(
        Long topicId,
        String name,
        String description,
        String address,
        String legalDongCode,
        String latitude,
        String longitude,
        List<String> images
) {
}
