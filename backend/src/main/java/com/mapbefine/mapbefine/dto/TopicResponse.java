package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.Topic;
import java.time.LocalDateTime;

public record TopicResponse(
        long id,
        String name,
        String image,
        int pinCount,
        LocalDateTime updatedAt
) {
    public static TopicResponse from(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getImageUrl(),
                topic.countPins(),
                topic.getUpdatedAt()
        );
    }
}
