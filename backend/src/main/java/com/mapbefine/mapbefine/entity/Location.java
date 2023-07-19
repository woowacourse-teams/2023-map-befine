package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Location extends BaseEntity {

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

    @OneToMany(mappedBy = "location")
    private List<Pin> pins = new ArrayList<>();

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
  
    public BigDecimal getLatitude() {
        return coordinate.getLatitude();
    }

    public BigDecimal getLongitude() {
        return coordinate.getLongitude();
    }

}
