package com.mapbefine.mapbefine.common.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends GlobalException {
    public InternalServerException(ErrorCode<?> errorCode) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
