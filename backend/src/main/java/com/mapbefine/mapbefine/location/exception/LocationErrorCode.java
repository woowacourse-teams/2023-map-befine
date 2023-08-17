package com.mapbefine.mapbefine.location.exception;

import lombok.Getter;

@Getter
public enum LocationErrorCode {

    ILLEGAL_COORDINATE_RANGE("04000", "한국 내의 좌표만 입력해주세요."),
    ;


    private final String code;
    private final String message;

    LocationErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
