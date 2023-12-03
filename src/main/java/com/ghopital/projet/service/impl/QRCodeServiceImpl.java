package com.ghopital.projet.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ghopital.projet.dto.response.QRCodeDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Material;
import com.ghopital.projet.entity.QRCode;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.MaterialRepository;
import com.ghopital.projet.repository.QRCodeRepository;
import com.ghopital.projet.service.QRCodeService;
import com.ghopital.projet.util.Constants;
import com.ghopital.projet.util.QRCodeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    private final MaterialRepository materialRepository;
    private final QRCodeRepository qrCodeRepository;
    public QRCodeServiceImpl(MaterialRepository materialRepository, QRCodeRepository qrCodeRepository) {
        this.materialRepository = materialRepository;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    @Transactional
    public QRCodeDtoResponse generateQRCodeMaterial(Long materialId) {
        // Get material from database
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material", "id", materialId.toString())
        );

        // check if already exist
        if(material.getQrCode() != null) {
            QRCode qrCode = material.getQrCode();

            material.setQrCode(null);

            // save update
            materialRepository.save(material);

            // Delete qr code
            qrCodeRepository.delete(qrCode);
        }

        // Get stock of material
        Stock stock = material.getStock();

        // Create an object mapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a JSON object
        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("id", material.getId());
        jsonObject.put("name", material.getName());
        jsonObject.put("description", material.getDescription());

        if(stock != null) {
            jsonObject.put("quantity", stock.getQuantity());

            if(stock.getLocation() != null) {
                jsonObject.put("location", stock.getLocation().getName());
            }
        }

        // Convert the JSON object to a string
        String qrCodeData = jsonObject.toString();

        // Create QRCode
        QRCode qrCode = new QRCode();

        byte[] qrCodeImage = QRCodeUtils.generateQRCode(qrCodeData, Constants.QR_CODE_WIDTH, Constants.QR_CODE_HEIGHT);

        qrCode.setQrCodeImage(qrCodeImage);

        // Save QR code in database
        QRCode savedQRCode = qrCodeRepository.save(qrCode);

        // Add QR code to material
        material.setQrCode(savedQRCode);

        // Save material update in database
        materialRepository.save(material);

        return new QRCodeDtoResponse(savedQRCode.getId(), savedQRCode.getQrCodeImage());
    }

    @Override
    @Transactional
    public byte[] getQRCodeMaterial(Long materialId) {
        // Get material from database
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material", "id", materialId.toString())
        );

        QRCodeDtoResponse response = this.generateQRCodeMaterial(materialId);

        return material.getQrCode().getQrCodeImage();
    }

    @Override
    public String readQRCodeMaterial(byte[] qrCodeImage) {
        String data = QRCodeUtils.readQRCode(qrCodeImage);

        if(!data.contains("material")) {
            throw new AppException(HttpStatus.NOT_FOUND, "Invalid QR code image");
        }

        return data;
    }
}
