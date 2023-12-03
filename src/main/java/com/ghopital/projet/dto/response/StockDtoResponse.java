package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * DTO for {@link com.ghopital.projet.entity.Stock}
 */
public record StockDtoResponse(Long id, Long quantity, ProductDto product,
                               LocationDto location) implements Serializable {
    /**
     * DTO for {@link com.ghopital.projet.entity.Product}
     */
    public record ProductDto(Long id, String name) implements Serializable {
    }

    /**
     * DTO for {@link com.ghopital.projet.entity.Location}
     */
    public record LocationDto(Long id, String name) implements Serializable {
    }
}