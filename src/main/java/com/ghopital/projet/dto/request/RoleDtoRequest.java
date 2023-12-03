package com.ghopital.projet.dto.request;

import com.ghopital.projet.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
public record RoleDtoRequest(
        @NotNull(message = "Role name is required")
        @NotBlank(message = "Role name cannot be blank")
        @Pattern(regexp = "^ROLE_.+", message = "Role name must start with '{regexp}'")
        String name
) implements Serializable { }