package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberInfo;

public record MemberResponse (
        Long id,
        String name,
        String email
) {

    public static MemberResponse from(Member member) {
        MemberInfo memberInfo = member.getMemberInfo();

        return new MemberResponse(
                member.getId(),
                memberInfo.getName(),
                memberInfo.getEmail()
        );
    }

}
