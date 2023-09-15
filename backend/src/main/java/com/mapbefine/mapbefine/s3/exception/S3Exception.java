package com.mapbefine.mapbefine.s3.exception;

import com.mapbefine.mapbefine.common.exception.BadRequestException;
import com.mapbefine.mapbefine.common.exception.ErrorCode;

public class S3Exception {

    public static class S3BadRequestException extends BadRequestException {

        public S3BadRequestException(S3ErrorCode errorCode) {
            super(new ErrorCode<>(errorCode.getCode(), errorCode.getMessage()));
        }
    }

}
