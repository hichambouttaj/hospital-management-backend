package com.ghopital.projet.dto.response;

import java.io.Serializable;

/**
 * A DTO for the {@link com.ghopital.projet.entity.QRCode} entity
 */
public record QRCodeDtoResponse(Long id, byte[] qrCodeImage) implements Serializable {
}