package com.mapbefine.mapbefine.topic.exception;

import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class TopicException extends GlobalException {

    public TopicException(
            HttpStatus status,
            ErrorCode errorCode
    ) {
        super(status, errorCode);
    }

    public static class TopicNotFoundException extends TopicException {
        public TopicNotFoundException(
                TopicErrorCode errorCode,
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
