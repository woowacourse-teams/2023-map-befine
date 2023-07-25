package com.mapbefine.mapbefine.dto;

import java.util.List;

public record TopicCreateRequest(
        String name,
        String image,
        String description,
        List<Long> pins
) {
}
