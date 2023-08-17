package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ForbiddenException extends GlobalException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }

}
