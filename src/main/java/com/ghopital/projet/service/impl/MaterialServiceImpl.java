package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.MaterialDtoRequest;
import com.ghopital.projet.dto.response.MaterialCreateDtoResponse;
import com.ghopital.projet.dto.response.MaterialDtoResponse;
import com.ghopital.projet.dto.response.QRCodeDtoResponse;
import com.ghopital.projet.entity.Material;
import com.ghopital.projet.entity.QRCode;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.MaterialMapper;
import com.ghopital.projet.repository.MaterialRepository;
import com.ghopital.projet.repository.QRCodeRepository;
import com.ghopital.projet.service.MaterialService;
import com.ghopital.projet.service.QRCodeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final QRCodeService qrCodeService;
    private final QRCodeRepository qrCodeRepository;
    public MaterialServiceImpl(MaterialRepository materialRepository, QRCodeService qrCodeService, QRCodeRepository qrCodeRepository) {
        this.materialRepository = materialRepository;
        this.qrCodeService = qrCodeService;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    public MaterialCreateDtoResponse createMaterial(MaterialDtoRequest materialDtoRequest) {
        // DTO to entity
        Material material = MaterialMapper.INSTANCE.materialDtoRequestToMaterial(materialDtoRequest);

        // Save material in database
        Material savedMaterial = materialRepository.save(material);

        // Generate QRCode for material
        QRCodeDtoResponse qrCodeDtoResponse = qrCodeService.generateQRCodeMaterial(savedMaterial.getId());

        // Get QRCode from database
        QRCode qrCode = qrCodeRepository.findById(qrCodeDtoResponse.id()).orElseThrow(
                () -> new ResourceNotFoundException("QRCode", "id", qrCodeDtoResponse.id().toString())
        );

        // Add QRCode to material
        material.setQrCode(qrCode);

        // Save material update
        Material updateMaterial = materialRepository.save(material);

        return MaterialMapper.INSTANCE.materialToMaterialCreateDtoResponse(updateMaterial);
    }

    @Override
    public MaterialDtoResponse getById(Long materialId) {
        // Get material from database
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material", "id", materialId.toString())
        );

        return MaterialMapper.INSTANCE.materialToMaterialDtoResponse(material);
    }

    @Override
    public List<MaterialDtoResponse> getAllMaterial() {
        // Get list of materials from database
        return materialRepository.findAll().stream()
                .map(MaterialMapper.INSTANCE::materialToMaterialDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialCreateDtoResponse updateMaterial(Long materialId, MaterialDtoRequest dto) {
        // Get material from database
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material", "id", materialId.toString())
        );

        if(StringUtils.hasText(dto.name())) {
            material.setName(dto.name());
        }

        if(StringUtils.hasText(dto.description())) {
            material.setDescription(dto.description());
        }

        // Save material update in database
        Material updatedMaterial = materialRepository.save(material);

        return MaterialMapper.INSTANCE.materialToMaterialCreateDtoResponse(updatedMaterial);
    }

    @Override
    public void deleteMaterial(Long materialId) {
        // Get material from database
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material", "id", materialId.toString())
        );

        // Delete material from database
        materialRepository.delete(material);
    }
}
