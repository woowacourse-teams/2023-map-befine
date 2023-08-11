package com.mapbefine.mapbefine.bookmark.dto.response;

import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import java.time.LocalDateTime;

public record BookmarkResponse(
        Long id,
        Long topicId,
        String name,
        String image,
        Integer pinCount,
        LocalDateTime updatedAt
) {

    public static BookmarkResponse from(Bookmark bookmark) {
        Topic topic = bookmark.getTopic();
        TopicInfo topicInfo = topic.getTopicInfo();

        return new BookmarkResponse(
                bookmark.getId(),
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getImageUrl(),
                topic.countPins(),
                topic.getUpdatedAt()
        );
    }

}
