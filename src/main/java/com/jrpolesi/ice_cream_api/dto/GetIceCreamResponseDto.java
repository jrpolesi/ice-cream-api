package com.jrpolesi.ice_cream_api.dto;

import java.math.BigDecimal;

public record GetIceCreamResponseDto(
    Integer id,
    String flavor,
    String size,
    BigDecimal price
) {
}