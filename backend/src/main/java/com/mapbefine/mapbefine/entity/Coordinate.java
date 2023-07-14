package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Coordinate extends BaseEntity {

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Coordinate(
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
