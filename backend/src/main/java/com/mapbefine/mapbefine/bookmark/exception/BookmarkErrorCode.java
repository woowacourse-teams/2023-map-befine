package com.mapbefine.mapbefine.bookmark.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BookmarkErrorCode {

    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 즐겨찾기를 찾을 수 없습니다.", "1000")
    ;

    private final HttpStatus status;
    private final String message;
    private final String code;

    BookmarkErrorCode(
            HttpStatus status,
            String message,
            String code
    ) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
