package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.DiplomaDtoRequest;
import com.ghopital.projet.dto.response.DiplomaDtoResponse;
import com.ghopital.projet.entity.Diploma;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DiplomaMapper {
    DiplomaMapper INSTANCE = Mappers.getMapper(DiplomaMapper.class);
    Diploma diplomaDtoRequestToDiploma(DiplomaDtoRequest diplomaDtoRequest);
    @Mappings({
            @Mapping(target = "documentId", source = "diploma.document.id"),
            @Mapping(target = "employeeId", source = "diploma.employee.id")
    })
    DiplomaDtoResponse diplomaToDiplomaDtoResponse(Diploma diploma);
}
