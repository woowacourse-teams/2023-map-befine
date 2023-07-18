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
			pin.getLocation().getParcelBaseAddress(),
			pin.getDescription(),
			pin.getLocation().getCoordinate().getLatitude().toString(),
			pin.getLocation().getCoordinate().getLongitude().toString()
		);
	}
}
