package com.mapbefine.mapbefine.oauth.domain;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import lombok.Getter;

@Getter
public class OauthMember {

    private String nickname;
    private String email;
    private String profileImageUrl;
    private OauthId oauthId;

    private OauthMember(
            String nickname,
            String email,
            String profileImageUrl,
            OauthId oauthId
    ) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.oauthId = oauthId;
    }

    public static OauthMember of(
            String nickname,
            String email,
            String profileImageUrl,
            Long oauthServerId,
            OauthServerType oauthServerType
    ) {
        return new OauthMember(
                nickname,
                email,
                profileImageUrl,
                new OauthId(oauthServerId, oauthServerType)
        );
    }

    public Member toRegisterMember() {
        return Member.ofRandomNickname(
                nickname,
                email,
                profileImageUrl,
                Role.USER,
                oauthId
        );
    }

}
