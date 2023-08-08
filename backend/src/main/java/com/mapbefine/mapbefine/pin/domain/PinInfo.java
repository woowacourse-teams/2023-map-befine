package com.mapbefine.mapbefine.pin.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PinInfo {

    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_NAME_LENGTH = 50;

    @Column(nullable = false, length = 50)
    private String name;

    @Lob
    @Column(nullable = false, length = 1000)
    private String description;

    private PinInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static PinInfo of(String name, String description) {
        validateName(name);
        validateDescription(description);

        return new PinInfo(name, description);
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name null");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름 길이 이상");
        }
    }

    private static void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description null");
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("description 길이 이상");
        }
    }

    public void update(String name, String description) {
        validateName(name);
        validateDescription(description);

        this.name = name;
        this.description = description;
    }
}
