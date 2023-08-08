package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.Image;
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
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름 길이 이상");
        }
    }

    private static void validateDescription(String description) {
        if (Objects.isNull(description)) {
            throw new IllegalArgumentException("설명은 null일 수 없습니다.");
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("description 길이 이상");
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
}
