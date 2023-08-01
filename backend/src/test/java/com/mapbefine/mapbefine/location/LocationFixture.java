package com.mapbefine.mapbefine.location;

import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import java.math.BigDecimal;

public class LocationFixture {

    public static Location create() {
        return new Location(
                new Address(
                        "지번주소",
                        "도로명주소",
                        "111000"
                ),
                Coordinate.of(35, 127)
        );
    }

    public static Location createByCoordinate(double latitude, double longitude) {
        return new Location(
                new Address(
                        "지번주소",
                        "도로명주소",
                        "111000"
                ),
                Coordinate.of(latitude, longitude)
        );
    }
}
