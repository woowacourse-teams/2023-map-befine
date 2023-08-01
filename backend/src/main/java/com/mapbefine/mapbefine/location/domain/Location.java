package com.mapbefine.mapbefine.location.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.pin.Domain.Pin;
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
public class Location extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Embedded
    private Coordinate coordinate;

    @OneToMany(mappedBy = "location")
    private List<Pin> pins = new ArrayList<>();

    public Location(Address address, Coordinate coordinate) {
        this.address = address;
        this.coordinate = coordinate;
    }

    public static Location of(
            String parcelBaseAddress,
            String roadBaseAddress,
            String legalDongCode,
            double latitude,
            double longitude
    ) {
        return new Location(
                new Address(
                        parcelBaseAddress,
                        roadBaseAddress,
                        legalDongCode
                ),
                Coordinate.of(
                        latitude,
                        longitude
                )
        );
    }

    public void addPin(Pin pin) {
        pins.add(pin);
    }

    public boolean isDuplicateCoordinate(Coordinate otherCoordinate) {
        return coordinate.isDuplicateCoordinate(otherCoordinate);
    }

    public boolean isSameAddress(String otherAddress) {
        return address.isSameAddress(otherAddress);
    }

    public double getLatitude() {
        return coordinate.getLatitude();
    }


    public double getLongitude() {
        return coordinate.getLongitude();
    }

}
