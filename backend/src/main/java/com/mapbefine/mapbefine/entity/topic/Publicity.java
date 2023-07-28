package com.mapbefine.mapbefine.entity.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;

@Getter
public enum Publicity {

    PUBLIC("같이 볼 지도"),
    PRIVATE("혼자 볼 지도");

    private final String title;

    Publicity(String title) {
        this.title = title;
    }

    @JsonCreator
    public static Publicity from(String input) {
        return Arrays.stream(values())
                .filter(publicity -> isSameName(input, publicity))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private static boolean isSameName(String input, Publicity publicity) {
        return publicity.name()
                .equalsIgnoreCase(input);
    }

}
