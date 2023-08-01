package com.mapbefine.mapbefine.location.domain;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static lombok.AccessLevel.PROTECTED;

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

    private static final double EARTH_RADIUS = 6371;
    private static final double UNIT_FOR_CONVERT_RADIAN = Math.PI / 180;
    private static final double DUPLICATE_STANDARD_DISTANCE = 0.01;
    private static final double UNIT_FOR_CONVERT_TO_CENTIMETER = Math.pow(10, 5);

//    @Column(precision = 10, scale = 7) // 상의해야 할 부분
    private double latitude;
//    @Column(precision = 10, scale = 7)
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
            throw new IllegalArgumentException("한국 내의 좌표만 입력해주세요.");
        }
    }

    private static boolean isNotInRange(double latitude, double longitude) {
        return (latitude < LATITUDE_LOWER_BOUND || LATITUDE_UPPER_BOUND < latitude)
                || (longitude < LONGITUDE_LOWER_BOUND || LONGITUDE_UPPER_BOUND < longitude);
    }

    /*
     * 오차 범위 2%
     * */
    public double calculateDistance(Coordinate otherCoordinate) {
        double result = applyHaversine(otherCoordinate);

        return convertToCentimeter(result);
    }

    private double applyHaversine(Coordinate otherCoordinate) {
        return acos(
                sin(toRadians(otherCoordinate.latitude)) * sin(toRadians(this.latitude)) +
                        (cos(toRadians(otherCoordinate.latitude)) * cos(toRadians(this.latitude))
                                * cos(toRadians(otherCoordinate.longitude - this.longitude)))
        ) * EARTH_RADIUS;
    }

    private double convertToCentimeter(double distance) {
        return distance * UNIT_FOR_CONVERT_TO_CENTIMETER;
    }

    public boolean isDuplicateCoordinate(Coordinate otherCoordinate) {
        return calculateDistance(otherCoordinate)
                <= convertToCentimeter(DUPLICATE_STANDARD_DISTANCE);
    }

    public static double getDuplicateStandardDistance() {
        return DUPLICATE_STANDARD_DISTANCE;
    }

}
