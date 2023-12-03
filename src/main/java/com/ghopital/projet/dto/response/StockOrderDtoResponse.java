package com.ghopital.projet.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.ghopital.projet.entity.StockOrder}
 */
public record StockOrderDtoResponse(String code, LocalDateTime orderDate, Set<DeliveryNoteDto> deliveryNotes,
                                    SupplierDto supplier, String deliveryLocation) implements Serializable {
    /**
     * DTO for {@link com.ghopital.projet.entity.DeliveryNote}
     */
    public record DeliveryNoteDto(Long id, ProductDto product, Long quantity) implements Serializable {
        /**
         * DTO for {@link com.ghopital.projet.entity.Product}
         */
        public record ProductDto(Long id, String name) implements Serializable {
        }
    }

    /**
     * DTO for {@link com.ghopital.projet.entity.Supplier}
     */
    public record SupplierDto(Long id, String name) implements Serializable {
    }
}