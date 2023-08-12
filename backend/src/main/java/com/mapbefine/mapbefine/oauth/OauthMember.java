package com.mapbefine.mapbefine.oauth;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import lombok.Getter;

@Getter
public class OauthMember {

    private String email;
    private String profileImageUrl;
    private OauthId oauthId;

    private OauthMember(
            String email,
            String profileImageUrl,
            OauthId oauthId
    ) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.oauthId = oauthId;
    }

    public static OauthMember of(
            String email,
            String profileImageUrl,
            Long oauthServerId,
            OauthServerType oauthServerType
    ) {
        return new OauthMember(email, profileImageUrl, new OauthId(oauthServerId, oauthServerType));
    }

    public Member toRegisterMember() {
        return Member.ofRandomNickname(
                email,
                profileImageUrl,
                Role.USER,
                oauthId
        );
    }

}
