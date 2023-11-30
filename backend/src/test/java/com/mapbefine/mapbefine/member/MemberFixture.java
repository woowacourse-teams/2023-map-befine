package com.mapbefine.mapbefine.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.domain.Status;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import com.mapbefine.mapbefine.topic.domain.Topic;

import java.util.List;

public class MemberFixture {

    public static Member create(String name, String email, Role role) {
        return Member.of(
                name,
                email,
                "https://map-befine-official.github.io/favicon.png",
                role,
                Status.NORMAL,
                new OauthId(1L, OauthServerType.KAKAO)
        );
    }

    public static Member createWithOauthId(String name, String email, Role role, OauthId oauthId) {
        return Member.of(
                name,
                email,
                "https://map-befine-official.github.io/favicon.png",
                role,
                Status.NORMAL,
                oauthId)
                ;
    }

    public static AuthMember createUser(Member member, List<Long> permittedTopicIds) {
        if (member.isAdmin()) {
            return new Admin(member.getId());
        }

        return new User(
                member.getId(),
                member.getCreatedTopics().stream().map(Topic::getId).toList(),
                permittedTopicIds
        );
    }

}
