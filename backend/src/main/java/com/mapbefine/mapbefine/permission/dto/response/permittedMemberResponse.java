package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;

public record permittedMemberResponse(
        Long id,
        MemberResponse memberResponse
) {

    public static permittedMemberResponse of(Long id, MemberResponse memberResponse) {
        return new permittedMemberResponse(
                id,
                memberResponse
        );
    }

}
