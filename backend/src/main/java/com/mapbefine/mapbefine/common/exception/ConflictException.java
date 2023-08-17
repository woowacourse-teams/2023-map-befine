package com.mapbefine.mapbefine.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ConflictException extends GlobalException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.CONFLICT);
    }

}
