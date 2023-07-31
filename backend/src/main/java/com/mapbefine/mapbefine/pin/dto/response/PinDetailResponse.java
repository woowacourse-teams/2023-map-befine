package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinImage;
import com.mapbefine.mapbefine.pin.Domain.PinInfo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PinDetailResponse(
        Long id,
        String name,
        String address,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime updatedAt,
        List<String> images
) {
    public static PinDetailResponse from(Pin pin) {
        List<String> images = pin.getPinImages().stream()
                .map(PinImage::getImageUrl)
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
