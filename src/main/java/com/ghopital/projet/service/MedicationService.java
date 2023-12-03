package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.MedicationDtoRequest;
import com.ghopital.projet.dto.response.MedicationDtoResponse;

import java.util.List;

public interface MedicationService {
    MedicationDtoResponse createMedication(MedicationDtoRequest request);

    MedicationDtoResponse getById(Long medicationId);

    List<MedicationDtoResponse> getAllMedication();

    MedicationDtoResponse updateMedication(Long medicationId, MedicationDtoRequest request);

    void deleteMedication(Long medicationId);
}
