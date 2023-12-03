package com.ghopital.projet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDto (
        @NotNull(message = "Cin is required")
        @NotBlank(message = "Cin should not be blank")
        String cin,
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password should not be blank")
        String password){
}
