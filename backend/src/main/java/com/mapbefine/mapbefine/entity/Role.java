package com.mapbefine.mapbefine.entity;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ROLE_ADMIN", "운영자"),
    USER("ROLE_USER", "로그인 유저"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String title;

    Role(String key, String title) {
        this.key = key;
        this.title = title;
    }

}
