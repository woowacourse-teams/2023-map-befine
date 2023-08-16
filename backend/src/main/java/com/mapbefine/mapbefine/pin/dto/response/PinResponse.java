package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinInfo;

public record PinResponse(
        Long id,
        String name,
        String address,
        String description,
        String creator,
        double latitude,
        double longitude
) {

    public static PinResponse from(Pin pin) {
        PinInfo pinInfo = pin.getPinInfo();

        return new PinResponse(
                pin.getId(),
                pinInfo.getName(),
                pin.getRoadBaseAddress(),
                pinInfo.getDescription(),
                pin.getCreator().getMemberInfo().getNickName(),
                pin.getLatitude(),
                pin.getLongitude()
        );
    }

}
