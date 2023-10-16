package com.mapbefine.mapbefine.pin.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;

public class PinCommentException {

    public static class PinCommentBadRequestException extends BadRequestException {
        public PinCommentBadRequestException(PinErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class PinCommentForbiddenException extends ForbiddenException {
        public PinCommentForbiddenException(PinErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class PinCommentNotFoundException extends ForbiddenException {
        public PinCommentNotFoundException(PinErrorCode errorCode, Long id) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage(), id));
        }
    }

}
