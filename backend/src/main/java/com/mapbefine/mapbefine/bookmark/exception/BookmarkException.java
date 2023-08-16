package com.mapbefine.mapbefine.bookmark.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;

public class BookmarkException {

    public static class BookmarkForbiddenException extends ForbiddenException {
        public BookmarkForbiddenException(BookmarkErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class BookmarkBadRequestException extends BadRequestException {
        public BookmarkBadRequestException(BookmarkErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}
