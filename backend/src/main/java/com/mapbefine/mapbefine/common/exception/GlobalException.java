package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus status;

    private final ErrorCode errorCode;

    public GlobalException(
            HttpStatus status,
            ErrorCode errorCode
    ) {
        super(errorCode.getMessage());
        this.status = status;
        this.errorCode = errorCode;
    }

}
