package com.mapbefine.mapbefine.member.dto.response;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberInfo;

public record MemberResponse(
        Long id,
        String nickName
) {
    /*
    * JPQL을 통해 객체를 생성할 때, 기본 생성자가 존재해야합니다.
    * Record 클래스를 사용할 경우, 이를 인식하지 못하는 것 같아요.
    * 해당 기본 생성자를 삭제하더라도, 오류가 발생하진 않지만, 경고 문구가 발생합니다. (빨간줄)
    * 이 부분 어떻게 할까요 ?
    * */

    public MemberResponse {
    }

    public static MemberResponse from(Member member) {
        MemberInfo memberInfo = member.getMemberInfo();

        return new MemberResponse(
                member.getId(),
                memberInfo.getNickName()
        );
    }

    public static MemberResponse of(Long id, String nickName) {
        return new MemberResponse(id, nickName);
    }

}
