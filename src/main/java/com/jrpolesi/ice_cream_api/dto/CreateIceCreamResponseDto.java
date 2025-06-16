package com.jrpolesi.ice_cream_api.dto;

import java.math.BigDecimal;

public record CreateIceCreamResponseDto (
    Integer id,
    String flavor,
    String size,
    BigDecimal price,
    Integer coneId
) {
}