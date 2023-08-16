package com.mapbefine.mapbefine.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorCode {

    ILLEGAL_NICKNAME_NULL("05001", "닉네임은 필수로 입력해야합니다."),
    ILLEGAL_NICKNAME_LENGTH("05002", "닉네임 길이는 최소 1 자에서 20자여야 합니다."),
    ILLEGAL_EMAIL_NULL("05003", "이메일은 필수로 입력해야합니다."),
    ILLEGAL_EMAIL_PATTERN("05004", "올바르지 않은 이메일 형식입니다."),
    ILLEGAL_ROLE_NULL("05005", "역할은 필수로 입력해야합니다."),
    MEMBER_NOT_FOUND("05401", "존재하지 않는 회원입니다. 회원 id를 확인하세요."),
    ;

    private final String code;
    private final String message;

    MemberErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
