package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinInfo;
import java.time.LocalDateTime;
import java.util.List;

public record PinDetailResponse(
        Long id,
        String name,
        String address,
        String description,
        double latitude,
        double longitude,
        LocalDateTime updatedAt,
        List<String> images
) {
    public static PinDetailResponse from(Pin pin) {
        List<String> images = pin.getPinImages().stream()
                .map(PinImage::getImage)
                .map(Image::getImageUrl)
                .toList();

        PinInfo pinInfo = pin.getPinInfo();

        return new PinDetailResponse(
                pin.getId(),
                pinInfo.getName(),
                pin.getRoadBaseAddress(),
                pinInfo.getDescription(),
                pin.getLatitude(),
                pin.getLongitude(),
                pin.getUpdatedAt(),
                images
        );
    }
}
