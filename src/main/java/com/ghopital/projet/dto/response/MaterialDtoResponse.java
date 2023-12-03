package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * DTO for {@link com.ghopital.projet.entity.Material}
 */
public record MaterialDtoResponse(Long id, String name, String description, StockDto stock) implements Serializable {
    /**
     * DTO for {@link com.ghopital.projet.entity.Stock}
     */
    public record StockDto(Long id, Long quantity, LocationDto location) implements Serializable {
        /**
         * DTO for {@link com.ghopital.projet.entity.Location}
         */
        public record LocationDto(Long id, String name) implements Serializable {
        }
    }
}