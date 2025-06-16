package com.jrpolesi.ice_cream_api.dto;

import com.jrpolesi.ice_cream_api.entities.Cone;

public record CreateConeResponseDto(
                Integer id,
                String type,
                String size) {

        public static CreateConeResponseDto fromEntity(Cone cone) {
                return new CreateConeResponseDto(
                                cone.getId(),
                                cone.getType(),
                                cone.getSize());
        }
}
