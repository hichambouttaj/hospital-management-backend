package com.ghopital.projet.dto.response;

import com.ghopital.projet.entity.Role;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
public record RoleDtoResponse(Long id, String name) implements Serializable {
}