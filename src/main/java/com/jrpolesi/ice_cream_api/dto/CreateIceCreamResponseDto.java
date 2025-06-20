package com.jrpolesi.ice_cream_api.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jrpolesi.ice_cream_api.entities.IceCream;

public record CreateIceCreamResponseDto(
        Integer id,
        String flavor,
        String size,
        BigDecimal price,
        @JsonProperty("cone_id") Integer coneId) {

    public static CreateIceCreamResponseDto fromEntity(IceCream iceCream) {
        return new CreateIceCreamResponseDto(
                iceCream.getId(),
                iceCream.getFlavor(),
                iceCream.getSize(),
                iceCream.getPrice(),
                iceCream.getCone().getId());
    }
}