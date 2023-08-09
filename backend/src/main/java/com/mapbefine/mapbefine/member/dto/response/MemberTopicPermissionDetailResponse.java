package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import java.time.LocalDateTime;

public record MemberTopicPermissionDetailResponse(
        Long id,
        LocalDateTime updatedAt,
        MemberDetailResponse memberDetailResponse
) {

    public static MemberTopicPermissionDetailResponse from(MemberTopicPermission memberTopicPermission) {
        return new MemberTopicPermissionDetailResponse(
                memberTopicPermission.getId(),
                memberTopicPermission.getUpdatedAt(),
                MemberDetailResponse.from(memberTopicPermission.getMember())
        );
    }

}
