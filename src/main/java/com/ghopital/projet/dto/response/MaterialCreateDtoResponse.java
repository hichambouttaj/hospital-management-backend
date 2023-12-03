package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Material} entity
 */
public record MaterialCreateDtoResponse(Long id, String name, String description) implements Serializable {
}