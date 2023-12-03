package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.ServiceDtoResponse;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.ServiceMapper;
import com.ghopital.projet.repository.ServiceRepository;
import com.ghopital.projet.service.IHospitalServices;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class IHospitalServicesImpl implements IHospitalServices {
    private final ServiceRepository serviceRepository;
    public IHospitalServicesImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ServiceDtoResponse createService(ServiceDtoRequest serviceDto) {
        // DTO to entity
        HospitalService hospitalService = ServiceMapper.INSTANCE.serviceDtoRequestToService(serviceDto);

        // save to db
        HospitalService savedHospitalService = serviceRepository.save(hospitalService);

        return ServiceMapper.INSTANCE.serviceToServiceDtoResponse(savedHospitalService);
    }

    @Override
    public ServiceDtoResponse getServiceById(Long serviceId) {
        HospitalService hospitalService = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service", "id", serviceId.toString())
        );
        return ServiceMapper.INSTANCE.serviceToServiceDtoResponse(hospitalService);
    }

    @Override
    public ServiceDtoResponse getServiceByName(String serviceName) {
        HospitalService hospitalService = serviceRepository.findByName(serviceName).orElseThrow(
                () -> new ResourceNotFoundException("Service", "id", serviceName)
        );
        return ServiceMapper.INSTANCE.serviceToServiceDtoResponse(hospitalService);
    }

    @Override
    public List<ServiceDtoResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(ServiceMapper.INSTANCE::serviceToServiceDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDtoResponse updateService(ServiceDtoRequest serviceDto, Long serviceId) {
        HospitalService hospitalService = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service", "id", serviceId.toString())
        );

        if(StringUtils.hasText(serviceDto.name())) {
            hospitalService.setName(serviceDto.name());
        }

        HospitalService updatedHospitalService = serviceRepository.save(hospitalService);
        return ServiceMapper.INSTANCE.serviceToServiceDtoResponse(updatedHospitalService);
    }

    @Override
    public void deleteService(Long serviceId) {
        HospitalService hospitalService = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service", "id", serviceId.toString())
        );
        serviceRepository.delete(hospitalService);
    }
}
