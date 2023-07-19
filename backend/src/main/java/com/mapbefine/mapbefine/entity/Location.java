package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Location extends BaseEntity {

	@OneToMany(mappedBy = "location")
	private final List<Pin> pins = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String parcelBaseAddress;
	@Column(nullable = false)
	private String roadBaseAddress;
	@Embedded
	private Coordinate coordinate;
	@Column(nullable = false)
	private String legalDongCode;

	public Location(
		String parcelBaseAddress,
		String roadBaseAddress,
		Coordinate coordinate,
		String legalDongCode
	) {
		this.parcelBaseAddress = parcelBaseAddress;
		this.roadBaseAddress = roadBaseAddress;
		this.coordinate = coordinate;
		this.legalDongCode = legalDongCode;
	}

	public void addPin(Pin pin) {
		pins.add(pin);
	}

}
