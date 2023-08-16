package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends RuntimeException {


    private static final HttpStatus status = HttpStatus.UNAUTHORIZED;
    private final ErrorCode<Long> errorCode;

    public UnauthorizedException(ErrorCode<Long> errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
