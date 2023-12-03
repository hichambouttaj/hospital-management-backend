package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.ServiceDtoResponse;
import com.ghopital.projet.service.IHospitalServices;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class HospitalServiceController {
    private final IHospitalServices hospitalServices;
    public HospitalServiceController(IHospitalServices hospitalServices) {
        this.hospitalServices = hospitalServices;
    }
    @PostMapping
    public ResponseEntity<ServiceDtoResponse> createService(@Valid @RequestBody ServiceDtoRequest serviceDto) {
        ServiceDtoResponse response = hospitalServices.createService(serviceDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDtoResponse> getServiceById(@PathVariable(name = "serviceId") Long serviceId) {
        ServiceDtoResponse response = hospitalServices.getServiceById(serviceId);
        return ResponseEntity.ok(response);
    }
    @GetMapping(params = {"serviceName"})
    public ResponseEntity<ServiceDtoResponse> getServiceByName(@RequestParam String serviceName) {
        ServiceDtoResponse response = hospitalServices.getServiceByName(serviceName);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<ServiceDtoResponse>> getAllService() {
        return ResponseEntity.ok(hospitalServices.getAllServices());
    }
    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceDtoResponse> updateService(
            @PathVariable(name = "serviceId") Long serviceId, @Valid @RequestBody ServiceDtoRequest serviceDto) {
        ServiceDtoResponse response = hospitalServices.updateService(serviceDto, serviceId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable(name = "serviceId") Long serviceId) {
        hospitalServices.deleteService(serviceId);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
