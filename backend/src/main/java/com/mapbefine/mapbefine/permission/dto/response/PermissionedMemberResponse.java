package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.domain.Permission;

public record PermissionedMemberResponse(
        Long id,
        MemberResponse memberResponse
) {

    public static PermissionedMemberResponse from(Permission permission) {
        return new PermissionedMemberResponse(
                permission.getId(),
                MemberResponse.from(permission.getMember())
        );
    }

}
