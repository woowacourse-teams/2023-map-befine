package com.mapbefine.mapbefine.atlas.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;

public class AtlasException {

    public static class AtlasBadRequestException extends BadRequestException {
        public AtlasBadRequestException(AtlasErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class AtlasForbiddenException extends ForbiddenException {
        public AtlasForbiddenException(AtlasErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}
