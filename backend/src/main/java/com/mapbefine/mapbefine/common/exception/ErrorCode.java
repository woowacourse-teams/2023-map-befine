package com.mapbefine.mapbefine.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Getter
public class ErrorCode {

    private final String code;

    private final String message;

    public ErrorCode(
            String code,
            String message
    ) {
        this.code = code;
        this.message = message;
    }

}
