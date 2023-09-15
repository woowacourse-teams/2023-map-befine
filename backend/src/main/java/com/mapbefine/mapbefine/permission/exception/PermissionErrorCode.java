package com.mapbefine.mapbefine.permission.exception;

import lombok.Getter;

@Getter
public enum PermissionErrorCode {

    ILLEGAL_TOPIC_ID("07000", "유효하지 않은 지도입니다."),
    ILLEGAL_PERMISSION_ID("07001", "유효하지 않은 권한 정보입니다."),
    FORBIDDEN_ADD_PERMISSION_GUEST("07300", "로그인하지 않은 사용자는 권한을 줄 수 없습니다."),
    FORBIDDEN_ADD_PERMISSION("07301", "지도를 생성한 사용자가 아니면 권한을 줄 수 없습니다."),
    PERMISSION_NOT_FOUND("07400", "존재하지 않는 권한 정보입니다."),
    PERMISSION_FORBIDDEN_BY_NOT_ADMIN("07401", "어드민 계정만 접근 가능합니다."),
    ;

    private final String code;
    private final String message;

    PermissionErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
