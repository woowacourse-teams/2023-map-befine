package com.mapbefine.mapbefine.member.domain;

public enum Status {
    NORMAL("STATUS_NORMAL", "정상 사용자"),
    DELETE("STATAUS_DELETE", "탈퇴한 사용자"),
    BLOCKED("STATUS_BLOCKED", "차단된 사용자");

    private final String key;
    private final String title;

    Status(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
