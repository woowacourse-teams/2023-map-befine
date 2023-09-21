package com.mapbefine.mapbefine.oauth.exception;

import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.NotFoundException;
import com.mapbefine.mapbefine.oauth.domain.OauthServerType;

public class OauthException {

    public static class OauthNotFoundException extends NotFoundException {
        public OauthNotFoundException(OauthErrorCode errorCode, OauthServerType oauthServerType) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage(), oauthServerType));
        }
    }

}

