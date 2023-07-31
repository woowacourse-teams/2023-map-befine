package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinInfo;
import java.math.BigDecimal;

public record PinResponse(
        Long id,
        String name,
        String address,
        String description,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static PinResponse from(Pin pin) {
        PinInfo pinInfo = pin.getPinInfo();

        return new PinResponse(
                pin.getId(),
                pinInfo.getName(),
                pin.getRoadBaseAddress(),
                pinInfo.getDescription(),
                pin.getLatitude(),
                pin.getLongitude()
        );
    }
}
