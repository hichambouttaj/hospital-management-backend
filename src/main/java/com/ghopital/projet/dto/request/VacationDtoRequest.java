package com.ghopital.projet.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Vacation} entity
 */
public record VacationDtoRequest(
        @FutureOrPresent(message = "Start date must be in the future")
        LocalDateTime startDate,
        @FutureOrPresent(message = "End date must be in the future")
        LocalDateTime endDate,
        boolean isSickVacation
) implements Serializable {
    @AssertTrue(message = "End date must be after start date")
    @JsonIgnore
    private boolean isEndDateAfterStartDate() {
        return endDate.isAfter(startDate);
    }
}