package com.jrpolesi.ice_cream_api.dto;

import java.math.BigDecimal;

import com.jrpolesi.ice_cream_api.entities.Cone;

public record GetIceCreamResponseDto(
    Integer id,
    String flavor,
    String size,
    BigDecimal price,
    Cone cone) {
}