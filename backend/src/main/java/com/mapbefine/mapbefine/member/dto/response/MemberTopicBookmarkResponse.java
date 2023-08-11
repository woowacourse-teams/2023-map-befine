package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.MemberTopicBookmark;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import java.time.LocalDateTime;

public record MemberTopicBookmarkResponse(
        Long id,
        Long topicId,
        String name,
        String image,
        Integer pinCount,
        LocalDateTime updatedAt
) {

    public static MemberTopicBookmarkResponse from(MemberTopicBookmark bookmark) {
        Topic topic = bookmark.getTopic();
        TopicInfo topicInfo = topic.getTopicInfo();

        return new MemberTopicBookmarkResponse(
                bookmark.getId(),
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getImageUrl(),
                topic.countPins(),
                topic.getUpdatedAt()
        );
    }

}
