package com.jrpolesi.ice_cream_api.dto;

import com.jrpolesi.ice_cream_api.entities.Cone;

public record GetConeResponseDto(
    Integer id,
    String type,
    String size) {

  public static GetConeResponseDto fromEntity(Cone cone) {
    return new GetConeResponseDto(
        cone.getId(),
        cone.getType(),
        cone.getSize());
  }
}