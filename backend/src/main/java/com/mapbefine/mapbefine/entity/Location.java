package com.mapbefine.mapbefine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

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

    public boolean isDuplicateCoordinate(Coordinate otherCoordinate) {
        return coordinate.isDuplicateCoordinate(otherCoordinate);
    }

    public boolean isSameAddress(String otherAddress) {
        return roadBaseAddress.equals(otherAddress);
    }

    public BigDecimal getLatitude() {
        return coordinate.getLatitude();
    }

    public BigDecimal getLongitude() {
        return coordinate.getLongitude();
    }

}
