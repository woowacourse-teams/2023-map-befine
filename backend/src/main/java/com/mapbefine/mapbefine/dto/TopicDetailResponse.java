package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.Topic;
import java.time.LocalDateTime;
import java.util.List;

public record TopicDetailResponse(
        Long id,
        String name,
        String description,
        String image,
        int pinCount,
        LocalDateTime updatedAt,
        List<PinResponse> pins
) {
    public static TopicDetailResponse from(Topic topic) {
        List<PinResponse> pinResponses = topic.getPins().stream()
                .map(PinResponse::from)
                .toList();

        return new TopicDetailResponse(
                topic.getId(),
                topic.getName(),
                topic.getDescription(),
                topic.getImageUrl(),
                topic.countPins(),
                topic.getUpdatedAt(),
                pinResponses
        );
    }
}
