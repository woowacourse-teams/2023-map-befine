package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.*;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Coordinate {

	private BigDecimal latitude;
	private BigDecimal longitude;

	public Coordinate(
		BigDecimal latitude,
		BigDecimal longitude
	) {
		validateLatitude(latitude);
		validateLongitude(longitude);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void validateLatitude(BigDecimal latitude) {
		if (isNotInRange(latitude, new BigDecimal("124"), new BigDecimal("132"))) {
			throw new IllegalArgumentException("한국 내의 좌표만 입력해주세요.");
		}
	}

	public void validateLongitude(BigDecimal longitude) {
		if (isNotInRange(longitude, new BigDecimal("33"), new BigDecimal("43"))) {
			throw new IllegalArgumentException("한국 내의 좌표만 입력해주세요.");
		}
	}

	private boolean isNotInRange(BigDecimal value, BigDecimal lowerBound, BigDecimal upperBound) {
		return value.compareTo(lowerBound) < 0 || value.compareTo(upperBound) > 0;
	}

}
