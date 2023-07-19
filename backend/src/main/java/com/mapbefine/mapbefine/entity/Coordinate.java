package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.DoubleUnaryOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
public class Coordinate {

    private static final BigDecimal EARTH_RADIUS = BigDecimal.valueOf(6371);
    private static final BigDecimal UNIT_FOR_CONVERT_RADIAN = BigDecimal.valueOf(Math.PI / 180);
    private static final BigDecimal DUPLICATE_STANDARD_DISTANCE = BigDecimal.valueOf(0.01);
    private static final BigDecimal UNIT_FOR_CONVERT_CENTIMETER = BigDecimal.valueOf(Math.pow(10, 5));

    @Column(precision = 18, scale = 15)
    private BigDecimal latitude;
    @Column(precision = 18, scale = 15)
    private BigDecimal longitude;

    public Coordinate(
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*
     * 오차 범위 2%
     * */
    public BigDecimal calculateDistance(Coordinate otherCoordinate) {
        BigDecimal deltaLatitude = convertToRadian(latitude.subtract(otherCoordinate.latitude));
        BigDecimal deltaLongitude = convertToRadian(longitude.subtract(otherCoordinate.longitude));
        BigDecimal sinDeltaLatitude = applyFormula(value -> Math.sin(value / 2), deltaLatitude);
        BigDecimal sinDeltaLongitude = applyFormula(value -> Math.sin(value / 2), deltaLongitude);

        BigDecimal squareRoot = applyFormula(Math::cos, convertToRadian(latitude))
                .multiply(applyFormula(Math::cos, convertToRadian(otherCoordinate.latitude)))
                .multiply(applyFormula(value -> Math.pow(value, 2), sinDeltaLongitude))
                .add(applyFormula(value -> Math.pow(value, 2), sinDeltaLatitude))
                .sqrt(MathContext.DECIMAL64);

        return convertToCentimeter(
                applyFormula(Math::asin, squareRoot)
                .multiply(BigDecimal.valueOf(2))
                .multiply(EARTH_RADIUS)
        );
    }

    private BigDecimal applyFormula(DoubleUnaryOperator formula, BigDecimal value) {
        return BigDecimal.valueOf(formula.applyAsDouble(value.doubleValue()));
    }

    private BigDecimal convertToRadian(BigDecimal value) {
        return value.multiply(UNIT_FOR_CONVERT_RADIAN);
    }

    private BigDecimal convertToCentimeter(BigDecimal distance) {
        return distance.multiply(UNIT_FOR_CONVERT_CENTIMETER);
    }

    public boolean isDuplicateCoordinate(Coordinate otherCoordinate) {
        return calculateDistance(otherCoordinate).doubleValue()
                <= convertToCentimeter(DUPLICATE_STANDARD_DISTANCE).doubleValue();
    }

    public static BigDecimal getDuplicateStandardDistance() {
        return DUPLICATE_STANDARD_DISTANCE;
    }

}
