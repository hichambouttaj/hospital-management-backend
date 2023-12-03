package com.ghopital.projet.dto.request;

import com.ghopital.projet.entity.HospitalService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * A DTO for the {@link HospitalService} entity
 */
public record ServiceDtoRequest(
        @NotNull(message = "Service name is required")
        @NotBlank(message = "Service name cannot be blank")
        String name
) implements Serializable {
}