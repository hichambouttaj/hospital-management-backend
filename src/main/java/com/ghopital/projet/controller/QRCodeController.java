package com.ghopital.projet.controller;

import com.ghopital.projet.dto.response.QRCodeDtoResponse;
import com.ghopital.projet.service.QRCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class QRCodeController {
    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }
    @PostMapping("/materials/{materialId}/qrCode")
    public ResponseEntity<QRCodeDtoResponse> generateQRCodeMaterial(
            @PathVariable(name = "materialId") Long materialId) {
        QRCodeDtoResponse response = qrCodeService.generateQRCodeMaterial(materialId);
        return ResponseEntity.created(URI.create("/materials/" + materialId + "/qrCode/" + response.id()))
                .body(response);
    }
    @GetMapping("/materials/{materialId}/qrCode")
    public ResponseEntity<byte[]> getQRCodeMaterial(@PathVariable(name = "materialId") Long materialId) {
        byte[] qrCodeImage = qrCodeService.getQRCodeMaterial(materialId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeImage);
    }
    @GetMapping("/qrCode/read")
    public ResponseEntity<String> readQRCode(@RequestPart(name = "file")MultipartFile file) throws IOException {
        byte[] qrCodeImage = file.getBytes();
        String response = qrCodeService.readQRCodeMaterial(qrCodeImage);
        return ResponseEntity.ok(response);
    }
}
