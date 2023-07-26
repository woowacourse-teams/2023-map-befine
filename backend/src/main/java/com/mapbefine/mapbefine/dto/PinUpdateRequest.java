package com.mapbefine.mapbefine.dto;

import java.util.List;

public record PinUpdateRequest(
        String name,
        String description,
        List<String> images
) {
}
