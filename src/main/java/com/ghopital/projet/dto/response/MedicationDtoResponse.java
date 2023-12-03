package com.ghopital.projet.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.ghopital.projet.entity.Medication}
 */
public record MedicationDtoResponse(Long id, String name, String description, StockDto stock, String manufacturer,
                                    BigDecimal price) implements Serializable {
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