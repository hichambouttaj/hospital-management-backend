package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.Supplier} entity
 */
public record SupplierDtoResponse(Long id, String name, String address, String phone,
                                  String email) implements Serializable {
}