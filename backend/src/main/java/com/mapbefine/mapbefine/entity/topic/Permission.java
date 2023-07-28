package com.mapbefine.mapbefine.entity.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;

@Getter
public enum Permission {

    ALL_MEMBERS("모든 회원"),
    GROUP_ONLY("소속 회원");

    private final String title;

    Permission(String title) {
        this.title = title;
    }

    @JsonCreator
    public static Publicity from(String input) {
        return Arrays.stream(values())
                .filter(permission -> isSameName(input, permission))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private static boolean isSameName(String input, Publicity permission) {
        return permission.name()
                .equalsIgnoreCase(input);
    }

}
