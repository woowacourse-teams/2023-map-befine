package com.mapbefine.mapbefine.auth.exception;

import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.UnauthorizedException;

public class AuthException {

    public static class AuthUnauthorizedException extends UnauthorizedException {
        public AuthUnauthorizedException(AuthErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}
