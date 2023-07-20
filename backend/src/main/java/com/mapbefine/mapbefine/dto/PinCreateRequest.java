package com.mapbefine.mapbefine.dto;

public record PinCreateRequest(
        Long topicId,
        String name,
        String description,
        String address,
        String legalDongCode,
        String latitude,
        String longitude
) {
}
