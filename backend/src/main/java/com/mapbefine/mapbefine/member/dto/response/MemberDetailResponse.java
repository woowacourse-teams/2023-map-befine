package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberInfo;
import java.time.LocalDateTime;

public record MemberDetailResponse(
        Long id,
        String nickName,
        String email,
        String imageUrl,
        LocalDateTime updatedAt
) {
    public static MemberDetailResponse from(Member member) {
        MemberInfo memberInfo = member.getMemberInfo();
        
        return new MemberDetailResponse(
                member.getId(),
                memberInfo.getNickName(),
                memberInfo.getEmail(),
                memberInfo.getImageUrl(),
                member.getUpdatedAt()
        );
    }

}
