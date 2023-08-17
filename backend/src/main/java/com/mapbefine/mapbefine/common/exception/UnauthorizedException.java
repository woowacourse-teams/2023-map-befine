package com.mapbefine.mapbefine.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GlobalException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }

}
