package com.mapbefine.mapbefine.member.dto.request;

import com.mapbefine.mapbefine.member.domain.Role;

public record MemberCreateRequest(
    String nickName,
    String email,
    String imageUrl,
    Role role
) {
}
