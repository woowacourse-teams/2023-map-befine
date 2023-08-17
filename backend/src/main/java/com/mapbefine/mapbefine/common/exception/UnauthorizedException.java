package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends GlobalException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }

}
