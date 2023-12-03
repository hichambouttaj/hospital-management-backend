package com.ghopital.projet.dto.response;

import com.ghopital.projet.entity.HospitalService;

import java.io.Serializable;

/**
 * A DTO for the {@link HospitalService} entity
 */
public record ServiceDtoResponse(Long id, String name) implements Serializable {
}