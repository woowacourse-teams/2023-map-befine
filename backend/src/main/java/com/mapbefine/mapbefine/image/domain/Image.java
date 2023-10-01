package com.mapbefine.mapbefine.image.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.exception.BadRequestException.ImageBadRequestException;
import com.mapbefine.mapbefine.common.util.RegexUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Image {

    private static final String VALID_IMAGE_URL_REGEX = "https?://.+";

    @Pattern(regexp = VALID_IMAGE_URL_REGEX)
    @Column(nullable = false, length = 2048)
    private String imageUrl;

    private Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Image from(String imageUrl) {
        validateUrl(imageUrl);

        return new Image(imageUrl);
    }

    private static void validateUrl(String imageUrl) {
        if (RegexUtil.matches(VALID_IMAGE_URL_REGEX, imageUrl)) {
            return;
        }

        throw new ImageBadRequestException();
    }

}
