package com.mapbefine.mapbefine.member.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.NotFoundException;

public class MemberException {

    public static class MemberBadRequestException extends BadRequestException {
        public MemberBadRequestException(MemberErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class MemberNotFoundException extends NotFoundException {
        public MemberNotFoundException(MemberErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}

