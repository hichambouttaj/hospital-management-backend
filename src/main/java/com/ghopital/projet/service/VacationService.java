package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.VacationDtoRequest;
import com.ghopital.projet.dto.response.VacationDtoResponse;

import java.util.List;

public interface VacationService {
    VacationDtoResponse createVacation(Long employeeId, VacationDtoRequest vacationDto);
    VacationDtoResponse getVacationById(Long employeeId, Long vacationId);
    List<VacationDtoResponse> getEmployeeVacations(Long employeeId);
    List<VacationDtoResponse> getAllVacations();
    VacationDtoResponse updateVacation(Long employeeId, Long vacationId, VacationDtoRequest vacationDto);
    void deleteVacation(Long employeeId, Long vacationId);
}
