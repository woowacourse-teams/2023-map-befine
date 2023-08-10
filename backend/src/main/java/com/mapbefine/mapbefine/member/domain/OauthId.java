package com.mapbefine.mapbefine.member.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class OauthId {

    @Column(nullable = false)
    private Long oauthId;

    public OauthId(Long oauthId) {
        this.oauthId = oauthId;
    }

}
