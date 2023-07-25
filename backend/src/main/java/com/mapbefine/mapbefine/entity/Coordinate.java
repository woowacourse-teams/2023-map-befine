package com.mapbefine.mapbefine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.DoubleUnaryOperator;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Coordinate {

    private static final BigDecimal LATITUDE_LOWER_BOUND = BigDecimal.valueOf(33);
    private static final BigDecimal LATITUDE_UPPER_BOUND = BigDecimal.valueOf(43);
    private static final BigDecimal LONGITUDE_LOWER_BOUND = BigDecimal.valueOf(124);
    private static final BigDecimal LONGITUDE_UPPER_BOUND = BigDecimal.valueOf(132);

    private static final BigDecimal EARTH_RADIUS = BigDecimal.valueOf(6371);
    private static final BigDecimal UNIT_FOR_CONVERT_RADIAN = BigDecimal.valueOf(Math.PI / 180);
    private static final BigDecimal DUPLICATE_STANDARD_DISTANCE = BigDecimal.valueOf(0.01);
    private static final BigDecimal UNIT_FOR_CONVERT_TO_CENTIMETER = BigDecimal.valueOf(Math.pow(10, 5));

    @Column(precision = 18, scale = 15)
    private BigDecimal latitude;
    @Column(precision = 18, scale = 15)
    private BigDecimal longitude;

    public Coordinate(BigDecimal latitude, BigDecimal longitude) {
        validateRange(latitude, longitude);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinate from(String latitude, String longitude) {
        return new Coordinate(
                new BigDecimal(latitude),
                new BigDecimal(longitude)
        );
    }

    private void validateRange(BigDecimal latitude, BigDecimal longitude) {
        if (isNotInRange(latitude, longitude)) {
            throw new IllegalArgumentException("한국 내의 좌표만 입력해주세요.");
        }
    }

    private boolean isNotInRange(BigDecimal latitude, BigDecimal longitude) {
        return LATITUDE_LOWER_BOUND.compareTo(latitude) > 0 ||
                LATITUDE_UPPER_BOUND.compareTo(latitude) < 0 ||
                LONGITUDE_LOWER_BOUND.compareTo(longitude) > 0 ||
                LONGITUDE_UPPER_BOUND.compareTo(longitude) < 0;
    }

    /*
     * 오차 범위 2%
     * */
    public BigDecimal calculateDistance(Coordinate otherCoordinate) {
        BigDecimal result = applyHaversine(otherCoordinate);

        return convertToCentimeter(result);
    }

    private BigDecimal applyHaversine(Coordinate otherCoordinate) {
        BigDecimal sinDeltaLatitude = convertToSinDelta(latitude, otherCoordinate.latitude);
        BigDecimal sinDeltaLongitude = convertToSinDelta(longitude, otherCoordinate.longitude);

        BigDecimal squareRoot = applyFormula(Math::cos, convertToRadian(latitude))
                .multiply(applyFormula(Math::cos, convertToRadian(otherCoordinate.latitude)))
                .multiply(applyFormula(value -> Math.pow(value, 2), sinDeltaLongitude))
                .add(applyFormula(value -> Math.pow(value, 2), sinDeltaLatitude))
                .sqrt(MathContext.DECIMAL64);

        return applyFormula(Math::asin, squareRoot)
                .multiply(BigDecimal.valueOf(2))
                .multiply(EARTH_RADIUS);
    }

    private BigDecimal convertToSinDelta(BigDecimal x, BigDecimal y) {
        BigDecimal delta = convertToRadian(x.subtract(y));

        return applyFormula(value -> Math.sin(value / 2), delta);
    }

    private BigDecimal applyFormula(DoubleUnaryOperator formula, BigDecimal value) {
        return BigDecimal.valueOf(formula.applyAsDouble(value.doubleValue()));
    }

    private BigDecimal convertToRadian(BigDecimal value) {
        return value.multiply(UNIT_FOR_CONVERT_RADIAN);
    }

    private BigDecimal convertToCentimeter(BigDecimal distance) {
        return distance.multiply(UNIT_FOR_CONVERT_TO_CENTIMETER);
    }

    public boolean isDuplicateCoordinate(Coordinate otherCoordinate) {
        return calculateDistance(otherCoordinate).doubleValue()
                <= convertToCentimeter(DUPLICATE_STANDARD_DISTANCE).doubleValue();
    }

    public static BigDecimal getDuplicateStandardDistance() {
        return DUPLICATE_STANDARD_DISTANCE;
    }

}
