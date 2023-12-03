package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.MedicationDtoRequest;
import com.ghopital.projet.dto.response.MedicationDtoResponse;
import com.ghopital.projet.entity.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MedicationMapper {
    MedicationMapper INSTANCE = Mappers.getMapper(MedicationMapper.class);
    Medication medicationDtoRequestToMedication(MedicationDtoRequest request);
    MedicationDtoResponse medicationToMedicationDtoResponse(Medication medication);
}
