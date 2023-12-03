package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ImageToUserDto(
        @NotNull(message = "Image id is required")
        @Positive
        Long imageId) {
}
