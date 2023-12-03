package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Stock} entity
 */
public record StockDtoRequest(
        @NotNull(message = "Product is is required.")
        Long productId,
        @NotNull(message = "Quantity is required")
        @PositiveOrZero(message = "Quantity should be a positive number")
        Long quantity
) implements Serializable { }