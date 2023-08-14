package com.mapbefine.mapbefine.oauth.domain;

import static java.util.Locale.ENGLISH;

public enum OauthServerType {

    KAKAO,
    ;

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }

}

