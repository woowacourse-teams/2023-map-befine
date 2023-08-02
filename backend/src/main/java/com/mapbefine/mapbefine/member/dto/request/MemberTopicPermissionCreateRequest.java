package com.mapbefine.mapbefine.member.dto.request;

public record MemberTopicPermissionCreateRequest(
    Long topicId,
    Long memberId
) {
}
