package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.PinImage;

import java.time.LocalDateTime;
import java.util.List;

public record PinDetailResponse(
        Long id,
        String name,
        String address,
        String description,
        String latitude,
        String longitude,
        LocalDateTime updatedAt,
        List<String> images
) {
    public static PinDetailResponse from(Pin pin) {
        List<String> images = pin.getPinImages().stream()
                .map(PinImage::getImageUrl)
                .toList();

        return new PinDetailResponse(
                pin.getId(),
                pin.getName(),
                pin.getRoadBaseAddress(),
                pin.getDescription(),
                pin.getLatitude().toString(),
                pin.getLongitude().toString(),
                pin.getUpdatedAt(),
                images
        );
    }
}
