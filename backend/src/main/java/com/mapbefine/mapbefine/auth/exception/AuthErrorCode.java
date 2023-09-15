package com.mapbefine.mapbefine.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorCode {
    ILLEGAL_MEMBER_ID("03100", "로그인에 실패하였습니다."),
    ILLEGAL_TOKEN("03101", "로그인에 실패하였습니다."),
    FORBIDDEN_ADMIN_ACCESS("03102", "로그인에 실패하였습니다."),
    BLOCKING_MEMBER_ACCESS("03103", "로그인에 실패하였습니다."),
    ;

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
