package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.member.Role;

public class MemberFixture {

    public static final Member ADMIN_MEMBER = new Member(
            "운영자",
            "admin@naver.com",
            "image.url",
            Role.ADMIN
    );
    public static final Member USER_MEMBER = new Member(
            "사용자",
            "user@naver.com",
            "image.url",
            Role.USER
    );
    public static final Member GUEST_MEMBER = new Member(
            "게스트",
            "guest@naver.com",
            "image.url",
            Role.GUEST
    );

}
