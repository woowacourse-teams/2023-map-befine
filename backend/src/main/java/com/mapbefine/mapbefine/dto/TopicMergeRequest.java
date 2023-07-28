package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import java.util.List;

public record TopicMergeRequest(
        String name,
        String image,
        String description,
        Publicity publicity,
        Permission permission,
        List<Long> topics
) {
}
