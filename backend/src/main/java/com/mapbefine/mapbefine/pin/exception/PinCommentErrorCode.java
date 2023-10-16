package com.mapbefine.mapbefine.pin.exception;

public enum PinCommentErrorCode {

    ILLEGAL_CONTENT_NULL("11000", "핀 댓글의 내용은 필수로 입력해야합니다."),
    ILLEGAL_CONTENT_LENGTH("11001", "핀 댓글의 내용이 최소 1 자에서 최대 1000 자여야 합니다.")
    ;

    private final String code;
    private final String message;

    PinCommentErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
