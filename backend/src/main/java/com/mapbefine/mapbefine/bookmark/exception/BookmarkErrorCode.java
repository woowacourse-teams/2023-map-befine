package com.mapbefine.mapbefine.bookmark.exception;

import lombok.Getter;

@Getter
public enum BookmarkErrorCode {

    ILLEGAL_TOPIC_ID("02000", "유효하지 않은 지도입니다."),
    ILLEGAL_MEMBER_ID("02001", "유효하지 않은 회원입니다."),
    FORBIDDEN_TOPIC_ADD("02300", "즐겨찾기 추가 권한이 없습니다."),
    FORBIDDEN_TOPIC_DELETE("02301", "즐겨찾기 삭제 권한이 없습니다."),
    CONFLICT_TOPIC_ALREADY_ADD("02900", "이미 즐겨찾기로 등록된 지도입니다."),
    ;

    private final String code;
    private final String message;

    BookmarkErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
