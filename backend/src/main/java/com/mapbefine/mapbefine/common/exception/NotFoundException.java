package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {

    private static final HttpStatus status = HttpStatus.NOT_FOUND;
    private final ErrorCode<Long> errorCode;

    public NotFoundException(ErrorCode<Long> errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
