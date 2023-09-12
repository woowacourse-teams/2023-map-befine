package com.mapbefine.mapbefine.topic.dto.response;

import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import java.time.LocalDateTime;

public record TopicResponse(
        Long id,
        String name,
        String image,
        String creator,
        Integer pinCount,
        Boolean isInAtlas,
        Integer bookmarkCount,
        Boolean isBookmarked,
        LocalDateTime updatedAt
) {

    public static TopicResponse from(Topic topic, Boolean isInAtlas, Boolean isBookmarked) {
        TopicInfo topicInfo = topic.getTopicInfo();

        return new TopicResponse(
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getImageUrl(),
                topic.getCreator().getMemberInfo().getNickName(),
                topic.countPins(),
                isInAtlas,
                topic.countBookmarks(),
                isBookmarked,
                topic.getUpdatedAt()
        );
    }

    public static TopicResponse ofGuestQuery(Topic topic) {
        TopicInfo topicInfo = topic.getTopicInfo();

        return new TopicResponse(
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getImageUrl(),
                topic.getCreator().getMemberInfo().getNickName(),
                topic.countPins(),
                Boolean.FALSE,
                topic.countBookmarks(),
                Boolean.FALSE,
                topic.getUpdatedAt()
        );
    }

}
