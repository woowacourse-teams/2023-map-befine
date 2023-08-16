package com.mapbefine.mapbefine.permission.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;
import com.mapbefine.mapbefine.common.exception.NotFoundException;

public class PermissionException {

    public static class PermissionForbiddenException extends ForbiddenException {
        public PermissionForbiddenException(PermissionErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class PermissionBadRequestException extends BadRequestException {
        public PermissionBadRequestException(PermissionErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class PermissionNotFoundException extends NotFoundException {
        public PermissionNotFoundException(PermissionErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}
