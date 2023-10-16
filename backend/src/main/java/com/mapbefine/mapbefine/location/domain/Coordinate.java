package com.mapbefine.mapbefine.location.domain;

import static com.mapbefine.mapbefine.location.exception.LocationErrorCode.ILLEGAL_COORDINATE_RANGE;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.location.exception.LocationException.LocationBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Coordinate {

    private static final double LATITUDE_LOWER_BOUND = 33;
    private static final double LATITUDE_UPPER_BOUND = 43;
    private static final double LONGITUDE_LOWER_BOUND = 124;
    private static final double LONGITUDE_UPPER_BOUND = 132;

    /*
     * 4326은 데이터베이스에서 사용하는 여러 SRID 값 중, 일반적인 GPS기반의 위/경도 좌표를 저장할 때 쓰이는 값입니다.
     * */
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Column(columnDefinition = "geometry SRID 4326", nullable = false)
    private Point coordinate;

    private Coordinate(Point point) {
        this.coordinate = point;
    }


    public static Coordinate of(double latitude, double longitude) {
        validateRange(latitude, longitude);

        Point point = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude));

        return new Coordinate(point);
    }

    private static void validateRange(double latitude, double longitude) {
        if (isNotInRange(latitude, longitude)) {
            throw new LocationBadRequestException(ILLEGAL_COORDINATE_RANGE);
        }
    }

    private static boolean isNotInRange(double latitude, double longitude) {
        return (latitude < LATITUDE_LOWER_BOUND || LATITUDE_UPPER_BOUND < latitude)
                || (longitude < LONGITUDE_LOWER_BOUND || LONGITUDE_UPPER_BOUND < longitude);
    }

    public double getLatitude() {
        return coordinate.getY();
    }

    public double getLongitude() {
        return coordinate.getX();
    }

}
