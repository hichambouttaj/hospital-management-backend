package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.ServiceDtoResponse;

import java.util.List;

public interface IHospitalServices {
    ServiceDtoResponse createService(ServiceDtoRequest serviceDto);
    ServiceDtoResponse getServiceById(Long serviceId);
    ServiceDtoResponse getServiceByName(String serviceName);
    List<ServiceDtoResponse> getAllServices();
    ServiceDtoResponse updateService(ServiceDtoRequest serviceDto, Long serviceId);
    void deleteService(Long serviceId);
}
