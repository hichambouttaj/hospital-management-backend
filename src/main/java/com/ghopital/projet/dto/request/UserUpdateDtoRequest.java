package com.ghopital.projet.dto.request;

import com.ghopital.projet.entity.User;
import jakarta.validation.constraints.*;

/**
 * A DTO for the {@link User} entity
 */
public record UserUpdateDtoRequest(
        @NotEmpty(message = "First Name should not be empty")
        @NotBlank(message = "First Name should not be blank")
        @Size(min = 3, message = "First Name should have at least {min} characters")
        String firstName,
        @NotEmpty(message = "Last Name should not be empty")
        @NotBlank(message = "Last Name should not be blank")
        @Size(min = 3, message = "Last Name should have at least {min} characters")
        String lastName,
        @NotEmpty(message = "Cin should not be empty")
        @NotBlank(message = "Cin should not be blank")
        @Size(min = 5, message = "Cin should have at least {min} characters")
        String cin,
        @NotEmpty(message = "Email should not be null or empty")
        @NotBlank(message = "Email should not be null or blank")
        @Email(message = "Enter a valid email")
        String email,
        @NotNull(message = "Role is required")
        RoleDtoRequest role
){}