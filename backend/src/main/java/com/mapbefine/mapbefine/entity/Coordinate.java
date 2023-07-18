package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
public class Coordinate {

    private static final BigDecimal DUPLICATE_STANDARD_DISTANCE = BigDecimal.valueOf(0.0001);
    private static final BigDecimal UNIT_FOR_CONVERT_CENTIMETER = BigDecimal.valueOf(Math.pow(10, 7));
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
     * 오차 범위 100CM
     * */
    public BigDecimal calculateDistance(Coordinate otherCoordinate) {
        BigDecimal latitudeDiff = latitude.subtract(otherCoordinate.latitude);
        BigDecimal longitudeDiff = longitude.subtract(otherCoordinate.longitude);

        return calculateDiagonalDistance(convertToCentimeter(latitudeDiff), convertToCentimeter(longitudeDiff));
    }

    private BigDecimal convertToCentimeter(BigDecimal distance) {
        return distance.multiply(UNIT_FOR_CONVERT_CENTIMETER);
    }

    private BigDecimal calculateDiagonalDistance(BigDecimal width, BigDecimal height) {
        BigDecimal squaredHeight = height.pow(2);
        BigDecimal squaredWidth = width.pow(2);

        return squaredWidth.add(squaredHeight).sqrt(MathContext.DECIMAL64);
    }

    public boolean isDuplicateCoordinate(Coordinate otherCoordinate) {
        return calculateDistance(otherCoordinate).doubleValue()
                <= convertToCentimeter(DUPLICATE_STANDARD_DISTANCE).doubleValue();
    }

    public static BigDecimal getDuplicateStandardDistance() {
        return DUPLICATE_STANDARD_DISTANCE;
    }

}
