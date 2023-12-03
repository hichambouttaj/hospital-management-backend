package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.ServiceDtoResponse;
import com.ghopital.projet.entity.HospitalService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);
    HospitalService serviceDtoRequestToService(ServiceDtoRequest serviceDtoRequest);
    ServiceDtoResponse serviceToServiceDtoResponse(HospitalService hospitalService);
}
