package com.mapbefine.mapbefine.permission.dto.request;

import java.util.List;

public record PermissionRequest(
        Long topicId,
        List<Long> memberIds
) {
}
