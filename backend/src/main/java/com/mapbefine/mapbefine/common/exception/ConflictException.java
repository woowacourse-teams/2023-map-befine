package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ConflictException extends RuntimeException {

    private static final HttpStatus status = HttpStatus.CONFLICT;
    private final ErrorCode<Long> errorCode;

    public ConflictException(ErrorCode<Long> errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
