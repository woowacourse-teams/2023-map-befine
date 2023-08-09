package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.pin.domain.PinImage;
import java.util.List;
import java.util.stream.Collectors;

public record PinImageResponse(
        Long id,
        String imageUrl
) {

    public static PinImageResponse from(PinImage pinImage) {
        return new PinImageResponse(pinImage.getId(), pinImage.getImageUrl());
    }

    public static List<PinImageResponse> from(List<PinImage> pinImage) {
        return pinImage.stream()
                .map(PinImageResponse::from)
                .collect(Collectors.toList());
    }

}
