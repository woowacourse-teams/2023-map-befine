package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {

    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final ErrorCode<Long> errorCode;

    public BadRequestException(ErrorCode<Long> errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class ImageBadRequestException extends BadRequestException {
        public ImageBadRequestException() {
            super(new ErrorCode<>("03001", "잘못된 형식의 URL입니다."));
        }
    }

}
