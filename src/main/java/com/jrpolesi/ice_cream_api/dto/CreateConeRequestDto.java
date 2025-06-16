package com.jrpolesi.ice_cream_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateConeRequestDto(
        @NotNull @Size(min = 1, max = 50, message = "cone_type must be between 1 and 50 characters") @JsonProperty("cone_type") String type,
        @NotNull @Size(min = 1, max = 1, message = "size must be a single character") String size) {
}
