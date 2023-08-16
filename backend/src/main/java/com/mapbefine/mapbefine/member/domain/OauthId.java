package com.mapbefine.mapbefine.member.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class OauthId {

    @Column(nullable = false)
    private Long oauthServerId;

    @Enumerated(EnumType.STRING)
    private OauthServerType oauthServerType;

    public OauthId(
            Long oauthServerId,
            OauthServerType oauthServerType
    ) {
        this.oauthServerId = oauthServerId;
        this.oauthServerType = oauthServerType;
    }

}
