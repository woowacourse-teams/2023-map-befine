package com.mapbefine.mapbefine.oauth.exception;

import lombok.Getter;

@Getter
public enum OauthErrorCode {

    OAUTH_SERVER_TYPE_NOT_FOUND("06400", "지원하지 않는 소셜 로그인 타입입니다."),
    ;

    private final String code;
    private final String message;

    OauthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
