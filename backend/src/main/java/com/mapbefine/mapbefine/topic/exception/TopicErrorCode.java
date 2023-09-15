package com.mapbefine.mapbefine.topic.exception;

import lombok.Getter;

@Getter
public enum TopicErrorCode {
    ILLEGAL_TOPIC_ID("10000", "유효하지 않은 지도입니다."),
    ILLEGAL_NAME_NULL("10001", "지도 이름은 필수로 입력해야합니다."),
    ILLEGAL_NAME_LENGTH("10002", "지도 이름 길이는 최소 1자에서 20자여야 합니다."),
    ILLEGAL_DESCRIPTION_NULL("10003", "지도 설명은 필수로 입력해야합니다."),
    ILLEGAL_DESCRIPTION_LENGTH("10004", "지도 설명의 길이는 최소 1자에서 1000자여야 합니다."),
    ILLEGAL_PUBLICITY_NULL("10005", "지도의 공개 범위는 필수로 입력해야합니다."),
    ILLEGAL_PERMISSION_NULL("10006", "지도의 권한 설정은 필수로 입력해야합니다."),
    ILLEGAL_PERMISSION_FOR_PUBLICITY_PRIVATE("10007", "비공개 지도인 경우, 권한 설정이 소속 회원이어야합니다."),
    ILLEGAL_PUBLICITY_FOR_PERMISSION_ALL_MEMBERS("10008", "권한 범위가 모든 회원인 경우, 비공개 지도로 설정할 수 없습니다."),
    ILLEGAL_PERMISSION_UPDATE("10009", "권한 범위를 모든 회원에서 소속 회원으로 수정할 수 없습니다."),
    FORBIDDEN_TOPIC_CREATE("10300", "로그인하지 않은 사용자는 지도를 생성할 수 없습니다."),
    FORBIDDEN_TOPIC_UPDATE("10301", "지도 수정 권한이 없습니다."),
    FORBIDDEN_TOPIC_DELETE("10302", "지도 삭제 권한이 없습니다."),
    FORBIDDEN_TOPIC_READ("10302", "지도 조회 권한이 없습니다."),
    TOPIC_NOT_FOUND("10400", "지도가 존재하지 않습니다."),
    ;

    private final String code;
    private final String message;

    TopicErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
