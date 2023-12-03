package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.MaterialDtoRequest;
import com.ghopital.projet.dto.response.MaterialCreateDtoResponse;
import com.ghopital.projet.dto.response.MaterialDtoResponse;

import java.util.List;

public interface MaterialService {
    MaterialCreateDtoResponse createMaterial(MaterialDtoRequest materialDtoRequest);
    MaterialDtoResponse getById(Long materialId);
    List<MaterialDtoResponse> getAllMaterial();
    MaterialCreateDtoResponse updateMaterial(Long materialId, MaterialDtoRequest materialDtoRequest);
    void deleteMaterial(Long materialId);
}
