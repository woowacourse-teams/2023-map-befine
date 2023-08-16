package com.mapbefine.mapbefine.atlas.exception;

import lombok.Getter;

@Getter
public enum AtlasErrorCode {

    ILLEGAL_COORDINATE_RANGE("00000", "한국 내의 좌표만 입력해주세요."),
    ILLEGAL_TOPIC_ID("00001", "유효하지 않은 지도입니다."),
    ILLEGAL_MEMBER_ID("00002", "유효하지 않은 회원입니다."),
    FORBIDDEN_TOPIC_ADD("00300", "모아보기 추가 권한이 없습니다."),
    FORBIDDEN_TOPIC_READ("00301", "지도 조회 권한이 없습니다."),
    ;

    private final String code;
    private final String message;

    AtlasErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
