package com.mapbefine.mapbefine.oauth.exception;

import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.NotFoundException;

public class OathException {

    public static class OauthNotFoundException extends NotFoundException {
        public OauthNotFoundException(OauthErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}

