package com.mapbefine.mapbefine.topic.dto.request;

import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import java.util.List;

public record TopicCreateRequest(
        String name,
        String image,
        String description,
        Publicity publicity,
        Permission permission,
        List<Long> pins
) {
}
