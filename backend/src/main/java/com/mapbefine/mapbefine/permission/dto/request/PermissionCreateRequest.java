package com.mapbefine.mapbefine.permission.dto.request;

public record PermissionCreateRequest(
        Long topicId,
        Long memberId
) {
}
