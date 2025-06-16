package com.jrpolesi.ice_cream_api.dto;

import java.math.BigDecimal;

import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.entities.IceCream;

public record GetIceCreamResponseDto(
        Integer id,
        String flavor,
        String size,
        BigDecimal price,
        Cone cone) {

    public static GetIceCreamResponseDto fromEntity(IceCream iceCream) {
        return new GetIceCreamResponseDto(
                iceCream.getId(),
                iceCream.getFlavor(),
                iceCream.getSize(),
                iceCream.getPrice(),
                iceCream.getCone());
    }
}