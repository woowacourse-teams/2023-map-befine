package com.mapbefine.mapbefine.member;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;

public class MemberFixture {

    public static Member create(Role role) {
        return new Member(
                "member",
                "member@naver.com",
                "image.com",
                role
        );
    }

}
