package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.Pin;
import java.time.LocalDateTime;

public record PinDetailResponse(
        Long id,
        String name,
        String address,
        String description,
        String latitude,
        String longitude,
        LocalDateTime updatedAt
) {
    public static PinDetailResponse from(Pin pin) {
        return new PinDetailResponse(
                pin.getId(),
                pin.getName(),
                pin.getRoadBaseAddress(),
                pin.getDescription(),
                pin.getLatitude().toString(),
                pin.getLongitude().toString(),
                pin.getUpdatedAt()
        );
    }
}
