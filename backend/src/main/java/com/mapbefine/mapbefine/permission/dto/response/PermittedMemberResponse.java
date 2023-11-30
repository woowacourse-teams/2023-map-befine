package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;

public record PermittedMemberResponse(
        Long id,
        MemberResponse memberResponse
) {

    public static PermittedMemberResponse of(Long id, MemberResponse memberResponse) {
        return new PermittedMemberResponse(
                id,
                memberResponse
        );
    }

}
