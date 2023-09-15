package com.mapbefine.mapbefine.auth.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;

public record LoginInfoResponse(
        String accessToken,
        MemberDetailResponse member
) {
}
