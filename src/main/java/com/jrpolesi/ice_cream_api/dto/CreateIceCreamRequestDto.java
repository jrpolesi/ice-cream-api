package com.jrpolesi.ice_cream_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIceCreamRequestDto(
        @NotNull @Size(min = 1, max = 50, message = "Flavor must be between 1 and 50 characters") String flavor,
        @NotNull @Size(min = 1, max = 1, message = "Size must be a single character") String size,
        @NotNull @Min(value = 0, message = "Price must be a positive value") BigDecimal price,
        @NotNull(message = "coneId is required") Integer coneId) {
}
