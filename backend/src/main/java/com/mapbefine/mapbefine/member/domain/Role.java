package com.mapbefine.mapbefine.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.NoSuchElementException;
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

    @JsonCreator
    public static Role from(String input) {
        return Arrays.stream(values())
                .filter(role -> isSameName(input, role))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static boolean isSameName(String input, Role role) {
        return role.name()
                .equalsIgnoreCase(input);
    }

}
