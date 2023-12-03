package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link com.ghopital.projet.entity.DeliveryNote}
 */
public record DeliveryNoteDtoRequest(
        @NotNull(message = "Order code is required.")
        @NotEmpty(message = "Order code should not be empty.")
        @NotBlank(message = "Order code should not be blank.")
        String orderCode,
        @NotNull(message = "Product id is required.")
        Long productId,
        @NotNull(message = "Quantity is required.")
        @Min(message = "Quantity should be at least 1.", value = 1)
        @Positive(message = "Quantity should be greater than 0.")
        Long quantity
) implements Serializable {
}