package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinInfo;
import java.time.LocalDateTime;
import java.util.List;

public record PinDetailResponse(
        Long id,
        String name,
        String address,
        String description,
        String creator,
        double latitude,
        double longitude,
        LocalDateTime updatedAt,
        List<PinImageResponse> images
) {

    public static PinDetailResponse from(Pin pin) {
        PinInfo pinInfo = pin.getPinInfo();

        return new PinDetailResponse(
                pin.getId(),
                pinInfo.getName(),
                pin.getRoadBaseAddress(),
                pinInfo.getDescription(),
                pin.getCreator().getMemberInfo().getNickName(),
                pin.getLatitude(),
                pin.getLongitude(),
                pin.getUpdatedAt(),
                PinImageResponse.from(pin.getPinImages())
        );
    }

}
