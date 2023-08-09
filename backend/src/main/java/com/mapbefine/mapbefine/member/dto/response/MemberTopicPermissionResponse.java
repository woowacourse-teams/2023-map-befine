package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;

public record MemberTopicPermissionResponse(
        Long id,
        MemberResponse memberResponse
) {

    public static MemberTopicPermissionResponse from(MemberTopicPermission memberTopicPermission) {
        return new MemberTopicPermissionResponse(
                memberTopicPermission.getId(),
                MemberResponse.from(memberTopicPermission.getMember())
        );
    }

}
