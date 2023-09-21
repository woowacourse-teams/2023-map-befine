package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.permission.domain.Permission;
import java.time.LocalDateTime;

public record PermissionMemberDetailResponse(
        Long id,
        LocalDateTime updatedAt,
        MemberDetailResponse memberDetailResponse
) {

    public static PermissionMemberDetailResponse from(Permission permission) {
        return new PermissionMemberDetailResponse(
                permission.getId(),
                permission.getUpdatedAt(),
                MemberDetailResponse.from(permission.getMember())
        );
    }

}
