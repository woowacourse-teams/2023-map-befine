package com.mapbefine.mapbefine.image.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;

public class ImageException {

    public static class ImageBadRequestException extends BadRequestException {

        public ImageBadRequestException(ImageErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }

    }

}
