package com.mapbefine.mapbefine.topic.dto.response;

import com.mapbefine.mapbefine.pin.domain.Pin;

public record RenderPinResponse(
        Long id,
        String name,
        Long topicId
) {

    public static RenderPinResponse from(Pin pin) {
        return new RenderPinResponse(
                pin.getId(),
                pin.getPinInfo().getName(),
                pin.getTopic().getId()
        );
    }

}
