package com.mapbefine.mapbefine.member;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;

public class MemberFixture {

    public static Member create(String name, String email, Role role) {
        return Member.of(
                name,
                email,
                "https://map-befine-official.github.io/favicon.png",
                role
        );
    }

}
