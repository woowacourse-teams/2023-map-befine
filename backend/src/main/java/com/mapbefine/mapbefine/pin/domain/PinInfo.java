package com.mapbefine.mapbefine.pin.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import java.util.Objects;
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
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("핀 이름은 필수입니다.");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("핀 이름의 길이는 1자 이상 " + MAX_NAME_LENGTH + "자 이하여야 합니다.");
        }
    }

    private static void validateDescription(String description) {
        if (Objects.isNull(description)) {
            throw new IllegalArgumentException("핀 설명은 필수입니다.");
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("핀 설명의 길이는 1자 이상 " + MAX_NAME_LENGTH + "자 이하여야 합니다.");
        }
    }
}
