package com.mapbefine.mapbefine.oauth.dto;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;

public record LoginInfoResponse(
        String accessToken,
        MemberDetailResponse member
) {

    public static LoginInfoResponse of(String accessToken, Member member) {
        return new LoginInfoResponse(accessToken, MemberDetailResponse.from(member));
    }

}
