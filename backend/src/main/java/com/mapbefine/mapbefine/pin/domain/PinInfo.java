package com.mapbefine.mapbefine.pin.domain;


import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_DESCRIPTION_LENGTH;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_DESCRIPTION_NULL;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_NAME_LENGTH;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_NAME_NULL;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
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
            throw new PinBadRequestException(ILLEGAL_NAME_NULL);
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new PinBadRequestException(ILLEGAL_NAME_LENGTH);
        }
    }

    private static void validateDescription(String description) {
        if (Objects.isNull(description)) {
            throw new PinBadRequestException(ILLEGAL_DESCRIPTION_NULL);
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new PinBadRequestException(ILLEGAL_DESCRIPTION_LENGTH);
        }
    }
}
