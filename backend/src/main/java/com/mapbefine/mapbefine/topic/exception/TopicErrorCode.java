package com.mapbefine.mapbefine.topic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TopicErrorCode {

    TOPIC_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 토픽을 찾을 수 없습니다.", "0000")
    ;

    private final HttpStatus status;
    private final String message;
    private final String code;

    TopicErrorCode(
            HttpStatus status,
            String message,
            String code
    ) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
