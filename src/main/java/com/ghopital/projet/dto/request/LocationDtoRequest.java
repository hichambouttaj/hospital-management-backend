package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Location} entity
 */
public record LocationDtoRequest(
        @NotNull(message = "Name is required")
        @NotBlank(message = "Name should not be blank")
        @Size(min = 3, max = 20, message = "Name must be between {min} and {max} characters")
        String name,
        @NotNull(message = "Description is required")
        @NotBlank(message = "Description should not be blank")
        @Size(min = 5, max = 255, message = "Description must be between {min} and {max} characters")
        String description,
        @NotNull(message = "Address is required")
        @NotBlank(message = "Address should not be blank")
        @Size(min = 5, max = 50, message = "Address must be between {min} and {max} characters")
        String address,
        @NotNull(message = "Latitude is required")
        @NotBlank(message = "Latitude should not be blank")
        String latitude,
        @NotNull(message = "Longitude is required")
        @NotBlank(message = "Longitude should not be blank")
        String longitude
) implements Serializable { }