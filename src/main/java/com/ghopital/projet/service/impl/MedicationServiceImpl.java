package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.MedicationDtoRequest;
import com.ghopital.projet.dto.response.MedicationDtoResponse;
import com.ghopital.projet.entity.Medication;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.MedicationMapper;
import com.ghopital.projet.repository.MedicationRepository;
import com.ghopital.projet.service.MedicationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;
    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public MedicationDtoResponse createMedication(MedicationDtoRequest request) {
        // DTO to entity
        Medication medication = MedicationMapper.INSTANCE.medicationDtoRequestToMedication(request);

        // Save medication in database
        Medication savedMedication = medicationRepository.save(medication);

        return MedicationMapper.INSTANCE.medicationToMedicationDtoResponse(savedMedication);
    }

    @Override
    public MedicationDtoResponse getById(Long medicationId) {
        // Get medication from database
        Medication medication = medicationRepository.findById(medicationId).orElseThrow(
                () -> new ResourceNotFoundException("Medication", "id", medicationId.toString())
        );

        return MedicationMapper.INSTANCE.medicationToMedicationDtoResponse(medication);
    }

    @Override
    public List<MedicationDtoResponse> getAllMedication() {
        return medicationRepository.findAll().stream()
                .map(MedicationMapper.INSTANCE::medicationToMedicationDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicationDtoResponse updateMedication(Long medicationId, MedicationDtoRequest request) {
        // Get medication from database
        Medication medication = medicationRepository.findById(medicationId).orElseThrow(
                () -> new ResourceNotFoundException("Medication", "id", medicationId.toString())
        );

        if(StringUtils.hasText(request.name())) {
            medication.setName(request.name());
        }

        if(StringUtils.hasText(request.description())) {
            medication.setDescription(request.description());
        }

        if(StringUtils.hasText(request.manufacturer())) {
            medication.setManufacturer(request.manufacturer());
        }

        if(request.price() != null) {
            medication.setPrice(request.price());
        }

        // Save medication update in database
        Medication updatedMedication = medicationRepository.save(medication);

        return MedicationMapper.INSTANCE.medicationToMedicationDtoResponse(updatedMedication);
    }

    @Override
    public void deleteMedication(Long medicationId) {
        // Get medication from database
        Medication medication = medicationRepository.findById(medicationId).orElseThrow(
                () -> new ResourceNotFoundException("Medication", "id", medicationId.toString())
        );

        // Delete medication from database
        medicationRepository.delete(medication);
    }
}
