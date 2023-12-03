package com.ghopital.projet.service;

import com.ghopital.projet.dto.response.QRCodeDtoResponse;

public interface QRCodeService {
    QRCodeDtoResponse generateQRCodeMaterial(Long materialId);
    byte[] getQRCodeMaterial(Long materialId);
    String readQRCodeMaterial(byte[] qrCodeImage);
}
