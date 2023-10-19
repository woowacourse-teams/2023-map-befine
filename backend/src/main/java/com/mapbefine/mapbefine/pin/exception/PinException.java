package com.mapbefine.mapbefine.pin.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;

public class PinException {

    public static class PinBadRequestException extends BadRequestException {
        public PinBadRequestException(PinErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class PinForbiddenException extends ForbiddenException {
        public PinForbiddenException(PinErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class PinNotFoundException extends ForbiddenException {
        public PinNotFoundException(PinErrorCode errorCode, Long id) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage(), id));
        }
    }

}
