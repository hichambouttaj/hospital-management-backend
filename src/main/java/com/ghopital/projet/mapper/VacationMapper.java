package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.VacationDtoRequest;
import com.ghopital.projet.dto.response.VacationDtoResponse;
import com.ghopital.projet.entity.Vacation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VacationMapper {
    VacationMapper INSTANCE = Mappers.getMapper(VacationMapper.class);
    @Mappings({
            @Mapping(target = "sickVacation", source = "vacationDtoRequest.isSickVacation")
    })
    Vacation vacationDtoRequestToVacation(VacationDtoRequest vacationDtoRequest);
    @Mappings({
            @Mapping(target = "employeeId", source = "vacation.employee.id"),
            @Mapping(target = "isSickVacation", source = "vacation.sickVacation")
    })
    VacationDtoResponse vacationToVacationDtoResponse(Vacation vacation);
}
