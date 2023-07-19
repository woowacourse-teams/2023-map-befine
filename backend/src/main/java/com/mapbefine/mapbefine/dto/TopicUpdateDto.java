package com.mapbefine.mapbefine.dto;

public record TopicUpdateDto(
        Long id,
        String name,
        String description
) {
    public static TopicUpdateDto of(Long id, TopicUpdateRequest topicUpdateRequest) {
        return new TopicUpdateDto(
                id,
                topicUpdateRequest.name(),
                topicUpdateRequest.description()
        );
    }
}
