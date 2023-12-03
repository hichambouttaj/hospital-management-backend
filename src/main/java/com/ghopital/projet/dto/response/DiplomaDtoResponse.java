package com.ghopital.projet.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Diploma} entity
 */
public record DiplomaDtoResponse(Long id, String title, LocalDate startDate, LocalDate endDate,
                                 Long documentId, Long employeeId) implements Serializable {
}