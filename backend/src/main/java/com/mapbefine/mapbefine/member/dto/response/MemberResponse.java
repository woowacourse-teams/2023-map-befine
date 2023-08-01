package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.Member;

public record MemberResponse (
        Long id,
        String name,
        String email
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
