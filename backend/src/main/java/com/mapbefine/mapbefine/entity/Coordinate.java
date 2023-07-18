package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
public class Coordinate {

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

}
