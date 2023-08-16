package com.mapbefine.mapbefine.bookmark.exception;

import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class BookmarkException extends GlobalException {

    public BookmarkException(
            HttpStatus status,
            ErrorCode errorCode
    ) {
        super(status, errorCode);
    }

    public static class BookmarkNotFoundException extends BookmarkException {
        public BookmarkNotFoundException(
                BookmarkErrorCode errorCode,
                Long bookmarkId
        ) {
            super(
                    errorCode.getStatus(),
                    new ErrorCode<>(
                            errorCode.getCode(),
                            errorCode.getMessage(),
                            bookmarkId
                    )
            );
        }
    }

}
