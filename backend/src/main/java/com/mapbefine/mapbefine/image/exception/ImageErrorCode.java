package com.mapbefine.mapbefine.image.exception;

import lombok.Getter;

@Getter
public enum ImageErrorCode {

    ILLEGAL_IMAGE_FILE_EXTENSION("10000", "지원하지 않는 이미지 파일입니다."),
    IMAGE_FILE_IS_NULL("10001", "이미지가 선택되지 않았습니다.")
    ;

    private final String code;
    private final String message;

    ImageErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
