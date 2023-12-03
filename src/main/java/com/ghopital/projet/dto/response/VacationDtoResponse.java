package com.ghopital.projet.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Vacation} entity
 */
public record VacationDtoResponse(Long id, LocalDateTime startDate, LocalDateTime endDate,
                                  boolean isSickVacation, Long employeeId) implements Serializable {
}