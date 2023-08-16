package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ForbiddenException extends RuntimeException {

    private static final HttpStatus status = HttpStatus.FORBIDDEN;
    private final ErrorCode<Long> errorCode;

    public ForbiddenException(ErrorCode<Long> errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
