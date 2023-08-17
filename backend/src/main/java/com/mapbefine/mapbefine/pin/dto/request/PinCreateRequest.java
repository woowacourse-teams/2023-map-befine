package com.mapbefine.mapbefine.pin.dto.request;

public record PinCreateRequest(
        Long topicId,
        String name,
        String description,
        String address,
        String legalDongCode,
        double latitude,
        double longitude
) {
}
