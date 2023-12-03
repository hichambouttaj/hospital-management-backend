package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.ghopital.projet.entity.StockOrder}
 */
public record StockOrderDtoRequest(
        @NotNull(message = "Order date is required.")
        LocalDateTime orderDate,
        @NotNull(message = "Delivery notes is required.")
        @Size(min = 1, message = "Order should have at least {min} delivery notes")
        Set<DeliveryNoteDto> deliveryNotes,
        @NotNull(message = "Supplier is required.")
        Long supplierId,
        @NotNull(message = "Delivery location is required.")
        @NotEmpty(message = "Delivery location should not be empty.")
        @NotBlank(message = "Delivery location should not be blank.")
        String deliveryLocation) implements Serializable {
    /**
     * DTO for {@link com.ghopital.projet.entity.DeliveryNote}
     */
    public record DeliveryNoteDto(
            @NotNull(message = "Product is required.")
            Long productId,
            @NotNull(message = "Quantity is required.")
            @Positive(message = "Quantity should be greater than 0.")
            Long quantity) implements Serializable {
    }
}