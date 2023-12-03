package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * DTO for {@link com.ghopital.projet.entity.Stock}
 */
public record StockUpdateDtoRequest(
        @NotNull(message = "Quantity is required.")
        @PositiveOrZero(message = "Quantity must be greater than 0")
        Long quantity) implements Serializable {
}