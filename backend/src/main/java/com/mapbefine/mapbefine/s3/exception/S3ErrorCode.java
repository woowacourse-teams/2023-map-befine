package com.mapbefine.mapbefine.s3.exception;

import lombok.Getter;

@Getter
public enum S3ErrorCode {

    ILLEGAL_IMAGE_FILE_EXTENSION("09000", "이미지 파일이 아닙니다.."),
    ;

    private final String code;
    private final String message;

    S3ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
