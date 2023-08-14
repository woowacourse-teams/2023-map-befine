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
        Integer pinCount,
        LocalDateTime updatedAt,
        List<PinResponse> pins,
        Boolean isBookmarked,
        Integer bookmarkCount
) {

    public static TopicDetailResponse from(Topic topic, Boolean isBookmarked) {
        List<PinResponse> pinResponses = topic.getPins().stream()
                .map(PinResponse::from)
                .toList();

        TopicInfo topicInfo = topic.getTopicInfo();

        return new TopicDetailResponse(
                topic.getId(),
                topicInfo.getName(),
                topicInfo.getDescription(),
                topicInfo.getImageUrl(),
                topic.countPins(),
                topic.getUpdatedAt(),
                pinResponses,
                isBookmarked,
                topic.countBookmarks()
        );
    }

}
