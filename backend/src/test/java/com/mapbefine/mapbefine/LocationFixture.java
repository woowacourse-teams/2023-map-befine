package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.entity.Coordinate;
import com.mapbefine.mapbefine.entity.Location;
import java.math.BigDecimal;

public class LocationFixture {

    public static Location create() {
        return new Location("지번주소", "도로명주소", new Coordinate(BigDecimal.valueOf(35), BigDecimal.valueOf(127)), "111000");
    }

    public static Location createByCoordinate(double latitude, double longitude) {
        return new Location("지번주소", "도로명주소", new Coordinate(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude)), "111000");
    }
}
