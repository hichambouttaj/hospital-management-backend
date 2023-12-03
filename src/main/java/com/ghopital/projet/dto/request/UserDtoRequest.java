package com.ghopital.projet.dto.request;

import com.ghopital.projet.entity.User;
import com.ghopital.projet.validation.PasswordValid;
import jakarta.validation.constraints.*;

/**
 * A DTO for the {@link User} entity
 */
public record UserDtoRequest (
        @NotEmpty(message = "First Name is required")
        @NotBlank(message = "First Name should not be blank")
        @Size(min = 3, message = "First Name should have at least {min} characters")
        String firstName,
        @NotEmpty(message = "Last Name is required")
        @NotBlank(message = "Last Name should not be blank")
        @Size(min = 3, message = "Last Name should have at least {min} characters")
        String lastName,
        @NotEmpty(message = "Cin is required")
        @NotBlank(message = "Cin should not be blank")
        @Size(min = 5, message = "Cin should have at least {min} characters")
        String cin,
        @NotEmpty(message = "Email is required")
        @NotBlank(message = "Email should not be null or blank")
        @Email(message = "Email should be valid")
        String email,
        @NotEmpty(message = "Password is required")
        @NotBlank(message = "Password should not be blank")
        @PasswordValid
        String password,
        @NotNull(message = "Role is required")
        RoleDtoRequest role
){}