package com.jrpolesi.ice_cream_api.dto;

import jakarta.validation.constraints.Size;

public record CreateConeRequestDto(
    @Size(min = 1, max = 50, message = "Type must be between 1 and 50 characters") String type,
    @Size(min = 1, max = 1, message = "Size must be a single character") String size) {
}
