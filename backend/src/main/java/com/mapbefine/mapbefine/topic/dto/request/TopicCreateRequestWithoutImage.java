package com.mapbefine.mapbefine.topic.dto.request;

import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import java.util.List;

public record TopicCreateRequestWithoutImage(
        String name,
        String description,
        Publicity publicity,
        PermissionType permissionType,
        List<Long> pins
) {
}
