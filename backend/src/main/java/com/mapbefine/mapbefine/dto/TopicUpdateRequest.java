package com.mapbefine.mapbefine.dto;

public record TopicUpdateRequest(
        String name,
        String image,
        String description
) {
}
