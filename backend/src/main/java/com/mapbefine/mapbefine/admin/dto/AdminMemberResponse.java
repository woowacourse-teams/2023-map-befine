package com.mapbefine.mapbefine.admin.dto;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberInfo;
import java.time.LocalDateTime;

public record AdminMemberResponse(
        Long id,
        String nickName,
        String email,
        String imageUrl,
        LocalDateTime updatedAt
) {

    public static AdminMemberResponse from(Member member) {
        MemberInfo memberInfo = member.getMemberInfo();

        return new AdminMemberResponse(
                member.getId(),
                memberInfo.getNickName(),
                memberInfo.getEmail(),
                memberInfo.getImageUrl(),
                member.getUpdatedAt()
        );
    }

}
