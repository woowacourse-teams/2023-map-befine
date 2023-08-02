package com.mapbefine.mapbefine.member;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;

public class MemberFixture {

    public static Member create(Role role) {
        return Member.of(
                "member",
                "jakind@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                role
        );
    }

}
