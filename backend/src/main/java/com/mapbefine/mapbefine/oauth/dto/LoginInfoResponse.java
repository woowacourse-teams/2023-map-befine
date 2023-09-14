package com.mapbefine.mapbefine.oauth.dto;

import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;

public record LoginInfoResponse(
        String accessToken,
        MemberDetailResponse member
) {
}
