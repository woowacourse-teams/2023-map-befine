package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.domain.Permission;

public record PermissionResponse(
        Long id,
        MemberResponse memberResponse
) {

    public static PermissionResponse from(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                MemberResponse.from(permission.getMember())
        );
    }

}
