package com.mapbefine.mapbefine.topic.domain;

import lombok.Getter;

@Getter
public enum Permission {

    ALL_MEMBERS("모든 회원"),
    GROUP_ONLY("소속 회원");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public boolean isAllMembers() {
        return this == ALL_MEMBERS;
    }

    public boolean isGroupOnly() {
        return this == GROUP_ONLY;
    }
}
