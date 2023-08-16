package com.mapbefine.mapbefine.topic.exception;

import lombok.Getter;

@Getter
public enum TopicErrorCode {
    ILLEGAL_TOPIC_ID("09000", "유효하지 않은 지도입니다."),
    ILLEGAL_NAME_NULL("09001", "지도 이름은 필수로 입력해야합니다."),
    ILLEGAL_NAME_LENGTH("09002", "지도 이름 길이는 최소 1자에서 20자여야 합니다."),
    ILLEGAL_DESCRIPTION_NULL("09003", "지도 설명은 필수로 입력해야합니다."),
    ILLEGAL_DESCRIPTION_LENGTH("09004", "지도 설명의 길이는 최소 1자에서 1000자여야 합니다."),
    ILLEGAL_PUBLICITY_NULL("09005", "지도의 공개 범위는 필수로 입력해야합니다."),
    ILLEGAL_PERMISSION_NULL("09006", "지도의 권한 설정은 필수로 입력해야합니다."),
    ILLEGAL_PERMISSION_FOR_PUBLICITY_PRIVATE("09007", "비공개 지도인 경우, 권한 설정이 소속 회원이어야합니다."),
    ILLEGAL_PUBLICITY_FOR_PERMISSION_ALL_MEMBERS("09008", "권한 범위가 모든 회원인 경우, 비공개 지도로 설정할 수 없습니다."),
    ILLEGAL_PERMISSION_UPDATE("09009", "권한 범위를 모든 회원에서 소속 회원으로 수정할 수 없습니다."),
    FORBIDDEN_TOPIC_CREATE("09300", "로그인하지 않은 사용자는 지도를 생성할 수 없습니다."),
    FORBIDDEN_TOPIC_UPDATE("09301", "지도 수정 권한이 없습니다."),
    FORBIDDEN_TOPIC_DELETE("09302", "지도 삭제 권한이 없습니다."),
    FORBIDDEN_TOPIC_READ("09302", "지도 조회 권한이 없습니다."),
    TOPIC_NOT_FOUND("09400", "지도가 존재하지 않습니다."),

    ;

    private final String code;
    private final String message;

    TopicErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
