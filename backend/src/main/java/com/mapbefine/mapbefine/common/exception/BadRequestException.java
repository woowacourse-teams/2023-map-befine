package com.mapbefine.mapbefine.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends GlobalException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.BAD_REQUEST);
    }

    public static class ImageBadRequestException extends BadRequestException {
        public ImageBadRequestException() {
            super(new ErrorCode("03000", "잘못된 형식의 URL입니다."));
        }
    }

}
