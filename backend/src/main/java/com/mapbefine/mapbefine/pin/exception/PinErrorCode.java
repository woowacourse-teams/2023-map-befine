package com.mapbefine.mapbefine.pin.exception;

import lombok.Getter;

@Getter
public enum PinErrorCode {

    ILLEGAL_PIN_ID("08000", "유효하지 않은 핀입니다."),
    FORBIDDEN_PIN_CREATE("08300", "핀을 추가할 권한이 없습니다."),

    ;

    private final String code;
    private final String message;

    PinErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
