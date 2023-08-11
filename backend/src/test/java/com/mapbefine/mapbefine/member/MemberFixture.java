package com.mapbefine.mapbefine.member;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.oauth.OauthServerType;
import com.mapbefine.mapbefine.topic.domain.Topic;

public class MemberFixture {

    public static Member create(String name, String email, Role role) {
        return Member.of(
                name,
                email,
                "https://map-befine-official.github.io/favicon.png",
                role,
                new OauthId(1L, OauthServerType.KAKAO)
        );
    }

    public static Member createWithOauthId(String name, String email, Role role, OauthId oauthId) {
        return Member.of(
                name,
                email,
                "https://map-befine-official.github.io/favicon.png",
                role,
                oauthId)
                ;
    }

    public static AuthMember createUser(Member member) {
        return new User(
                member.getId(),
                member.getCreatedTopics().stream().map(Topic::getId).toList(),
                member.getTopicsWithPermissions().stream().map(Topic::getId).toList()
        );
    }

}
