package com.mapbefine.mapbefine.pin.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record PinImageCreateRequest(
        Long pinId,
        MultipartFile image
) {
}
