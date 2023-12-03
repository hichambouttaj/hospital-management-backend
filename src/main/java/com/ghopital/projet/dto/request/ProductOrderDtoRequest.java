package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.ghopital.projet.entity.ProductOrder}
 */
public record ProductOrderDtoRequest(
        @NotNull(message = "Order date is required.")
        LocalDateTime orderDate,
        @NotNull(message = "Delivery notes is required.")
        @Size(min = 1, message = "Order should have at least {min} delivery notes")
        Set<DeliveryNoteDto> deliveryNotes,
        @NotNull(message = "Hospital service is required.")
        @NotEmpty(message = "Hospital service should not be empty.")
        @NotBlank(message = "Hospital service should not be blank.")
        String hospitalServiceName) implements Serializable {
    /**
     * DTO for {@link com.ghopital.projet.entity.DeliveryNote}
     */
    public record DeliveryNoteDto(
            @NotNull(message = "Product is required.")
            Long productId,
            @NotNull(message = "Quantity is required.")
            @Positive(message = "Quantity should be greater than 0.")
            Long quantity
    ) implements Serializable { }
}