package com.mapbefine.mapbefine.pin.exception;

import lombok.Getter;

@Getter
public enum PinErrorCode {

    ILLEGAL_PIN_ID("08000", "유효하지 않은 핀입니다."),
    ILLEGAL_PIN_IMAGE_ID("08001", "유효하지 않은 핀 이미지 입니다."),
    ILLEGAL_NAME_NULL("08002", "핀 이름은 필수로 입력해야합니다."),
    ILLEGAL_NAME_LENGTH("08003", "핀 이름 길이는 최소 1자에서 50자여야 합니다."),
    ILLEGAL_DESCRIPTION_NULL("08004", "핀 설명은 필수로 입력해야합니다."),
    ILLEGAL_DESCRIPTION_LENGTH("08005", "핀 설명의 길이는 최소 1자에서 1000자여야 합니다."),
    FORBIDDEN_PIN_CREATE_OR_UPDATE("08300", "핀을 추가할 권한이 없습니다."),
    FORBIDDEN_PIN_READ("08301", "핀을 조회할 권한이 없습니다."),
    PIN_NOT_FOUND("08400", "존재하지 않는 핀입니다.");

    private final String code;
    private final String message;

    PinErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
