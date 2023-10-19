package com.mapbefine.mapbefine.location.domain;

import static com.mapbefine.mapbefine.location.exception.LocationErrorCode.ILLEGAL_COORDINATE_RANGE;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.location.exception.LocationException.LocationBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Coordinate {

    private static final double LATITUDE_LOWER_BOUND = 33;
    private static final double LATITUDE_UPPER_BOUND = 43;
    private static final double LONGITUDE_LOWER_BOUND = 124;
    private static final double LONGITUDE_UPPER_BOUND = 132;

    @Column(columnDefinition = "Decimal(18,15)")
    private double latitude;

    @Column(columnDefinition = "Decimal(18,15)")
    private double longitude;

    private Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinate of(double latitude, double longitude) {
        validateRange(latitude, longitude);

        return new Coordinate(latitude, longitude);
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

    public double calculateDistanceInMeters(Coordinate otherCoordinate) {
        double earthRadius = 6_371_000;

        return acos(sin(toRadians(otherCoordinate.latitude)) * sin(toRadians(this.latitude)) + (
                cos(toRadians(otherCoordinate.latitude)) * cos(toRadians(this.latitude))
                        * cos(toRadians(otherCoordinate.longitude - this.longitude))
        )) * earthRadius;
    }

}
