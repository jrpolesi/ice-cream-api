package com.jrpolesi.ice_cream_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jrpolesi.ice_cream_api.entities.Cone;

public record CreateConeResponseDto(
        Integer id,
        @JsonProperty("cone_type") String type,
        String size) {

    public static CreateConeResponseDto fromEntity(Cone cone) {
        return new CreateConeResponseDto(
                cone.getId(),
                cone.getType(),
                cone.getSize());
    }
}
