package com.ghopital.projet.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ghopital.projet.exception.AppException;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Diploma} entity
 */
public record DiplomaDtoRequest(
        @NotEmpty(message = "Title is required")
        @NotBlank(message = "Title should not be blank")
        String title,
        @NotNull(message = "Start date is required")
        @Past(message = "Start date should be in the past")
        LocalDate startDate,
        @NotNull(message = "End date is required")
        @Past(message = "End date should be in the past")
        LocalDate endDate
) implements Serializable {
    @AssertTrue(message = "Start date must be before than end date")
    @JsonIgnore
    private boolean isStartBeforeEnd() {
        boolean isStartBeforeEnd = startDate.isBefore(endDate);
        if(!isStartBeforeEnd) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Start date must be before than end date");
        }
        return true;
    }
}