package com.mapbefine.mapbefine.image.exception;

import lombok.Getter;

@Getter
public enum S3ErrorCode {

    ILLEGAL_IMAGE_FILE_EXTENSION("09000", "이미지 파일이 아닙니다."),
    IMAGE_FILE_IS_NULL("09001", "이미지가 선택되지 않았습니다.")
    ;

    private final String code;
    private final String message;

    S3ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
