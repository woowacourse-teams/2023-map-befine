package com.mapbefine.mapbefine.pin.dto.request;

import java.util.List;

public record PinUpdateRequest(
        String name,
        String description,
        List<String> images
) {
}
