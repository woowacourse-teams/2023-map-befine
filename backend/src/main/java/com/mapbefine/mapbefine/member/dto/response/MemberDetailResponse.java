package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.Member;
import java.time.LocalDateTime;

public record MemberDetailResponse(
        Long id,
        String name,
        String email,
        String imageUrl,
        LocalDateTime updateAt
) {
    public static MemberDetailResponse from(Member member) {
        return new MemberDetailResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getImageUrl(),
                member.getUpdatedAt()
        );
    }
}
