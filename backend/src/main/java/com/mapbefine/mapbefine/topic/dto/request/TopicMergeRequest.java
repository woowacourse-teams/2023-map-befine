package com.mapbefine.mapbefine.topic.dto.request;

import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record TopicMergeRequest(
        String name,
        MultipartFile image,
        String description,
        Publicity publicity,
        PermissionType permissionType,
        List<Long> topics
) {

    public static TopicMergeRequest of(
            TopicMergeRequestWithoutImage topicMergeRequestWithoutImage,
            MultipartFile image
    ) {
        return new TopicMergeRequest(
                topicMergeRequestWithoutImage.name(),
                image,
                topicMergeRequestWithoutImage.description(),
                topicMergeRequestWithoutImage.publicity(),
                topicMergeRequestWithoutImage.permissionType(),
                topicMergeRequestWithoutImage.topics()
        );
    }

}
