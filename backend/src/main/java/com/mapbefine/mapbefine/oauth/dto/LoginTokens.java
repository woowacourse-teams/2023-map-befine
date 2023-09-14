package com.mapbefine.mapbefine.oauth.dto;

import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;

public record LoginTokens(
        String accessToken,
        String refreshToken,
        MemberDetailResponse memberDetailResponse
) {

    public LoginInfoResponse toResponse() {
        return new LoginInfoResponse(accessToken, memberDetailResponse);
    }
}
