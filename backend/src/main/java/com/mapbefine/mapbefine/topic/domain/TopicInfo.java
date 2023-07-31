package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class TopicInfo {

    private static final String DEFAULT_IMAGE_URL = "https://map-befine-official.github.io/favicon.png";
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_NAME_LENGTH = 20;

    @Column(nullable = false, length = 20)
    private String name;

    @Lob
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 2048)
    private String imageUrl = DEFAULT_IMAGE_URL;

    private TopicInfo(
            String name,
            String description,
            String imageUrl
    ) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
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

    private static String validateImageUrl(String imageUrl) {
        if (imageUrl == null) { // TODO: 2023/07/28 URL 검증
            return DEFAULT_IMAGE_URL;
        }
        return imageUrl;
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
        this.imageUrl = validateImageUrl(imageUrl);
    }

}
