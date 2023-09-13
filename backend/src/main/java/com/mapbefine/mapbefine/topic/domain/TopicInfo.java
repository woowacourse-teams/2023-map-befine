package com.mapbefine.mapbefine.topic.domain;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_DESCRIPTION_LENGTH;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_DESCRIPTION_NULL;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_NAME_LENGTH;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_NAME_NULL;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Lob;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class TopicInfo {

    private static final Image DEFAULT_IMAGE =
            Image.from("https://map-befine-official.github.io/favicon.png");

    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_NAME_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String name;

    @Lob
    @Column(nullable = false, length = 1000)
    private String description;

    @Embedded
    private Image image;

    private TopicInfo(
            String name,
            String description,
            Image image
    ) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public static TopicInfo of(
            String name,
            String description,
            String imageUrl
    ) {
        validateName(name);
        validateDescription(description);

        return new TopicInfo(name, description, createImage(imageUrl));
    }

    private static void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new TopicBadRequestException(ILLEGAL_NAME_NULL);
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new TopicBadRequestException(ILLEGAL_NAME_LENGTH);
        }
    }

    private static void validateDescription(String description) {
        if (Objects.isNull(description)) {
            throw new TopicBadRequestException(ILLEGAL_DESCRIPTION_NULL);
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new TopicBadRequestException(ILLEGAL_DESCRIPTION_LENGTH);

        }
    }

    private static Image createImage(String imageUrl) {
        if (Objects.isNull(imageUrl)) {
            return DEFAULT_IMAGE;
        }

        return Image.from(imageUrl);
    }

    public String getImageUrl() {
        return image.getImageUrl();
    }

    public TopicInfo removeImage() {
        return new TopicInfo(
                this.name,
                this.description,
                DEFAULT_IMAGE
        );
    }

}
