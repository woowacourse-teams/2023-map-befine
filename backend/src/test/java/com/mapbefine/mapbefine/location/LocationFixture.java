package com.mapbefine.mapbefine.location;

import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;

public class LocationFixture {

    public static Location create() {
        return new Location(AddressFixture.create(), Coordinate.of(35, 127));
    }

    public static Location createByCoordinate(double latitude, double longitude) {
        return new Location(AddressFixture.create(), Coordinate.of(latitude, longitude));
    }

}
