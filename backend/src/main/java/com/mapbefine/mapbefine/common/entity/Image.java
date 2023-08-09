package com.mapbefine.mapbefine.common.entity;

import static lombok.AccessLevel.PROTECTED;

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

    private static final String VALID_IMAGE_URL_REGEX = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)";

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

        throw new IllegalArgumentException("잘못된 형식의 URL입니다.");
    }

}
