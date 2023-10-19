package com.mapbefine.mapbefine.topic.dto.request;

import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;

public record TopicUpdateRequest(
        String name,
        String image,
        String description,
        Publicity publicity,
        PermissionType permissionType
) {

}
