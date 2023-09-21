package com.mapbefine.mapbefine.topic.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;
import com.mapbefine.mapbefine.common.exception.ForbiddenException;
import com.mapbefine.mapbefine.common.exception.NotFoundException;
import java.util.List;

public class TopicException {
    public static class TopicBadRequestException extends BadRequestException {
        public TopicBadRequestException(TopicErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class TopicForbiddenException extends ForbiddenException {
        public TopicForbiddenException(TopicErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

    public static class TopicNotFoundException extends NotFoundException {
        public TopicNotFoundException(TopicErrorCode errorCode, List<Long> ids) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage(), ids));
        }
    }

}
