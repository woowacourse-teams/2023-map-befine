package com.mapbefine.mapbefine.topic.dto.response;

import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import java.time.LocalDateTime;
import java.util.List;

public record TopicDetailResponse(
        Long id,
        String name,
        String description,
        String image,
        String creator,
        Integer pinCount,
        Boolean isInAtlas,
        Integer bookmarkCount,
        Boolean isBookmarked,
        LocalDateTime updatedAt,
        List<PinResponse> pins
) {
    public static TopicDetailResponse from(Topic topic, Boolean isInAtlas, Boolean isBookmarked) {
        List<PinResponse> pinResponses = topic.getPins().stream()
                .map(PinResponse::from)
                .toList();

        TopicInfo topicInfo = topic.getTopicInfo();

        return new TopicDetailResponse(
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getDescription(),
                topicInfo.getImageUrl(),
                topic.getCreator().getMemberInfo().getNickName(),
                topic.countPins(),
                isInAtlas,
                topic.countBookmarks(),
                isBookmarked,
                topic.getUpdatedAt(),
                pinResponses
        );
    }
}
