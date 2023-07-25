package com.mapbefine.mapbefine.dto;

import com.mapbefine.mapbefine.entity.Pin;

public record PinResponse(
        Long id,
        String name,
        String address,
        String description,
        String latitude,
        String longitude
) {
    public static PinResponse from(Pin pin) {
        return new PinResponse(
                pin.getId(),
                pin.getName(),
                pin.getRoadBaseAddress(),
                pin.getDescription(),
                pin.getLatitude().toString(),
                pin.getLongitude().toString()
        );
    }
}
