package com.mapbefine.mapbefine.common.dto;

import com.mapbefine.mapbefine.common.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }

}
