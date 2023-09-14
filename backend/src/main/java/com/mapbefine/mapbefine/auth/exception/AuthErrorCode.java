package com.mapbefine.mapbefine.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorCode {
    ILLEGAL_MEMBER_ID("01100", "로그인에 실패하였습니다."),
    ILLEGAL_TOKEN("01101", "로그인에 실패하였습니다."),
    EXPIRED_TOKEN("01102", "기간이 만료된 토큰입니다."),
    ;

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
