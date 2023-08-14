package com.mapbefine.mapbefine.topic.dto.response;

import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import java.time.LocalDateTime;

public record TopicResponse(
        Long id,
        String name,
        String image,
        Integer pinCount,
        Integer bookmarkCount,
        Boolean isBookmarked,
        LocalDateTime updatedAt
) {

    public static TopicResponse from(Topic topic, Boolean isBookmarked) {
        TopicInfo topicInfo = topic.getTopicInfo();

        return new TopicResponse(
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getImageUrl(),
                topic.countPins(),
                topic.countBookmarks(),
                isBookmarked,
                topic.getUpdatedAt()
        );
    }

}
