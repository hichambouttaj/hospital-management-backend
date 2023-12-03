package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.File} entity
 */
public record FileDtoResponse(Long id, String name, String type, long size) implements Serializable {
}