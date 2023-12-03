package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Medication} entity
 */
public record MedicationDtoRequest(
        @NotNull(message = "Name is required")
        @NotBlank(message = "Name should not be blank")
        String name,
        @NotNull(message = "Description is required")
        @NotBlank(message = "Description should not be blank")
        @Size(min = 10, max = 255, message = "Description must be between {min} and {max} characters")
        String description,
        @NotNull(message = "Manufacturer is required")
        @NotBlank(message = "Manufacturer should not be blank")
        String manufacturer,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be a positive number")
        BigDecimal price
) implements Serializable {
}