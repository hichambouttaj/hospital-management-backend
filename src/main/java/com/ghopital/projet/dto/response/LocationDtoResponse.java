package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Location} entity
 */
public record LocationDtoResponse(Long id, String name, String description, String address, String latitude,
                                  String longitude) implements Serializable {
}