package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Employee} entity
 */
public record EmployeeDtoRequest(
        @NotNull(message = "First name is required")
        @NotBlank(message = "First name should not be blank")
        @Size(min = 3, message = "First name should at least contain {min} characters")
        String firstName,
        @NotNull(message = "Last name is required")
        @NotBlank(message = "Last name should not be blank")
        @Size(min = 3, message = "Last name should at least contain {min} characters")
        String lastName,
        @NotNull(message = "Cin is required")
        @NotBlank(message = "Cin should not be blank")
        @Size(min = 5, message = "Cin should at least contain {min} characters")
        String cin,
        @NotNull(message = "Registration number is required")
        @NotBlank(message = "Registration number should not be blank")
        @Size(min = 5, message = "Registration number should at least contain {min} characters")
        String registrationNumber,
        @NotNull(message = "Recruitment date is required")
        @Past(message = "Recruitment date should be in the past")
        LocalDate recruitmentDate,
        @NotNull(message = "Service is required")
        ServiceDtoRequest service
) implements Serializable { }