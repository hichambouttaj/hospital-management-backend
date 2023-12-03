package com.ghopital.projet.dto.response;

import com.ghopital.projet.entity.User;

import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
public record UserDtoResponse(Long id, String firstName, String lastName,
                              String email, String cin,
                              RoleDtoResponse role, Long imageId) implements Serializable {
}