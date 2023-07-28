package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.member.Role;
import com.mapbefine.mapbefine.entity.pin.Location;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.topic.Topic;

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
