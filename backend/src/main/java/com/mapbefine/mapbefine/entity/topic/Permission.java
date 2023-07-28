package com.mapbefine.mapbefine.entity.topic;

import lombok.Getter;

@Getter
public enum Permission {

    ALL_MEMBERS("모든 회원"),
    GROUP_ONLY("소속 회원");

    private final String title;

    Permission(String title) {
        this.title = title;
    }
}
