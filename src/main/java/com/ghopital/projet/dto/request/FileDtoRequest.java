package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.File} entity
 */
public record FileDtoRequest(
        @NotNull(message = "File id is required")
        @Positive
        Long id,
        @NotNull(message = "File type is required")
        @NotBlank(message = "File type should not be blank")
        String type
) implements Serializable { }