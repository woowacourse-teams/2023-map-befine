package com.mapbefine.mapbefine.topic.dto.request;

public record TopicUpdateRequest(
        String name,
        String image,
        String description
) {
}
