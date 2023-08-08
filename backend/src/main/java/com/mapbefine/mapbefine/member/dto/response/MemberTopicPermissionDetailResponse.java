package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;

public record MemberTopicPermissionDetailResponse(
        Long id,
        MemberDetailResponse memberDetailResponse
) {

    public static MemberTopicPermissionDetailResponse from(MemberTopicPermission memberTopicPermission) {
        return new MemberTopicPermissionDetailResponse(
                memberTopicPermission.getId(),
                MemberDetailResponse.from(memberTopicPermission.getMember())
        );
    }

}
