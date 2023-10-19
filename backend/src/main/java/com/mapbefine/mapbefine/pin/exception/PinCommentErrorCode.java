package com.mapbefine.mapbefine.pin.exception;

import lombok.Getter;

@Getter
public enum PinCommentErrorCode {

    ILLEGAL_CONTENT_NULL("11000", "핀 댓글의 내용은 필수로 입력해야합니다."),
    ILLEGAL_CONTENT_LENGTH("11001", "핀 댓글의 내용이 최소 1 자에서 최대 1000 자여야 합니다."),
    ILLEGAL_PIN_COMMENT_DEPTH("11002", "핀 대댓글에는 대댓글을 달 수 없습니다."),
    FORBIDDEN_PIN_COMMENT_CREATE("11003", "핀 댓글을 추가할 권한이 없습니다."),
    FORBIDDEN_PIN_COMMENT_UPDATE("11004", "핀 댓글을 수정할 권한이 없습니다."),
    FORBIDDEN_PIN_COMMENT_DELETE("11005", "핀 댓글을 삭제할 권한이 없습니다."),
    PIN_COMMENT_NOT_FOUND("11006", "존재하지 않는 핀 댓글입니다."),
    ;

    private final String code;
    private final String message;

    PinCommentErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
