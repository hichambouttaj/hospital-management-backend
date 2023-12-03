package com.ghopital.projet.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Employee} entity
 */
public record EmployeeDtoResponse(Long id, String firstName, String lastName, String cin, String registrationNumber,
                                  LocalDate recruitmentDate, String serviceName) implements Serializable {
}