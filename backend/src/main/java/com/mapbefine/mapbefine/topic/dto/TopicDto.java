package com.mapbefine.mapbefine.topic.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TopicDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String creatorNickName;
    private Integer pinCounts;
    private Integer bookmarkCounts;
    private LocalDateTime lastPinUpdatedAt;

    public TopicDto() {
    }

    public TopicDto(
            Long id,
            String name,
            String imageUrl,
            String creatorNickName,
            Integer pinCounts,
            Integer bookmarkCounts,
            LocalDateTime lastPinUpdatedAt
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.creatorNickName = creatorNickName;
        this.pinCounts = pinCounts;
        this.bookmarkCounts = bookmarkCounts;
        this.lastPinUpdatedAt = lastPinUpdatedAt;
    }

}
