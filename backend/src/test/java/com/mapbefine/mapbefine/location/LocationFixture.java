package com.mapbefine.mapbefine.location;

import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import java.util.List;

public class LocationFixture {

    public static final String PARCEL_ADDRESS = "지번 주소";
    public static final String ROAD_ADDRESS = "도로명 주소";
    public static final String LEGAL_DONG_CODE = "111000";

    public static final Address ADDRESS = new Address(
            PARCEL_ADDRESS,
            ROAD_ADDRESS,
            LEGAL_DONG_CODE
    );

    /**
     * BASE_COORDINATE 부터의 직선 거리
     * UNDER_METER: 직선 거리 약 0.669 미터,
     * SEVEN_METER: 직선 거리 약 7.565 미터,
     * EIGHTEEN_FORTY_SIX_METER: 직선 거리 약 1846.414 미터,
     * SIXTY_FOUR_THIRTY_METER: 직선 거리 약 6430.905 미터
     * */
    public static final Coordinate BASE_COORDINATE = Coordinate.of(37.6273677, 127.0447364);
    public static final Coordinate UNDER_ONE_METER = Coordinate.of(37.6273644, 127.0447427);
    public static final Coordinate SEVEN_METER = Coordinate.of(37.6274194, 127.0447922);
    public static final Coordinate EIGHTEEN_FORTY_SIX_METER = Coordinate.of(37.6316286, 127.0650012);
    public static final Coordinate SIXTY_FOUR_THIRTY_METER = Coordinate.of(37.6507643, 127.1115283);

    public static final List<Coordinate> COORDINATES = List.of(
            LocationFixture.UNDER_ONE_METER,
            LocationFixture.SEVEN_METER,
            LocationFixture.EIGHTEEN_FORTY_SIX_METER,
            LocationFixture.SIXTY_FOUR_THIRTY_METER
    );

    public static final Location LOCATION = new Location(ADDRESS, BASE_COORDINATE);

    public static Location create() {
        return new Location(ADDRESS, Coordinate.of(35, 127));
    }

    public static Location from(Coordinate coordinate) {
        return new Location(ADDRESS, coordinate);
    }

    public static Location createByCoordinate(double latitude, double longitude) {
        return new Location(ADDRESS, Coordinate.of(latitude, longitude));
    }

}
