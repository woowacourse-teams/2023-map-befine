package com.mapbefine.mapbefine.location.dto;

import java.math.BigDecimal;

public record CoordinateRequest(
        BigDecimal latitude,
        BigDecimal longitude
) {
}
