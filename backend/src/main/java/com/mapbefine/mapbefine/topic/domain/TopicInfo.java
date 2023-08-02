package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class TopicInfo {

    private static final Image DEFAULT_IMAGE = Image.from("https://map-befine-official.github.io/favicon.png");
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

        return new TopicInfo(
                name,
                description,
                validateImageUrl(imageUrl)
        );
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

    private static Image validateImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return DEFAULT_IMAGE;
        }
        return Image.from(imageUrl);
    }

    public void update(
            String name,
            String description,
            String imageUrl
    ) {
        validateName(name);
        validateDescription(description);

        this.name = name;
        this.description = description;
        this.image = validateImageUrl(imageUrl);
    }

    public String getImageUrl() {
        return image.getImageUrl();
    }
}
