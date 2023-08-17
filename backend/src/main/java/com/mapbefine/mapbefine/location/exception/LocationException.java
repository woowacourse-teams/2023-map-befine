package com.mapbefine.mapbefine.location.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;

public class LocationException {

    public static class LocationBadRequestException extends BadRequestException {

        public LocationBadRequestException(LocationErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}
