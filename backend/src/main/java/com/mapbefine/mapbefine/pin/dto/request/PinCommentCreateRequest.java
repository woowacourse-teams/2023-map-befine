package com.mapbefine.mapbefine.pin.dto.request;

public record PinCommentCreateRequest(
        Long pinId,
        Long parentPinCommentId,
        String content
) {
}
