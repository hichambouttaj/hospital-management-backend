package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.VacationDtoRequest;
import com.ghopital.projet.dto.response.VacationDtoResponse;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.Vacation;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.VacationMapper;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.VacationRepository;
import com.ghopital.projet.service.VacationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacationServiceImpl implements VacationService {
    private final VacationRepository vacationRepository;
    private final EmployeeRepository employeeRepository;
    public VacationServiceImpl(VacationRepository vacationRepository, EmployeeRepository employeeRepository) {
        this.vacationRepository = vacationRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public VacationDtoResponse createVacation(Long employeeId, VacationDtoRequest vacationDto) {
        // DTO to entity
        Vacation vacation = VacationMapper.INSTANCE.vacationDtoRequestToVacation(vacationDto);

        vacation.setSickVacation(vacationDto.isSickVacation());

        // get employee from db
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        // add employee to vacation
        vacation.setEmployee(employee);

        // save vacation in db
        Vacation savedVacation = vacationRepository.save(vacation);
        employeeRepository.save(employee);

        return VacationMapper.INSTANCE.vacationToVacationDtoResponse(savedVacation);
    }

    @Override
    public VacationDtoResponse getVacationById(Long employeeId, Long vacationId) {
        Vacation vacation = checkVacationByEmployee(employeeId, vacationId);
        return VacationMapper.INSTANCE.vacationToVacationDtoResponse(vacation);
    }

    @Override
    public List<VacationDtoResponse> getAllVacations() {
        return vacationRepository.findAll().stream()
                .map(VacationMapper.INSTANCE::vacationToVacationDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VacationDtoResponse> getEmployeeVacations(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );
        return employee.getVacations().stream()
                .map(VacationMapper.INSTANCE::vacationToVacationDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacationDtoResponse updateVacation(Long employeeId, Long vacationId, VacationDtoRequest vacationDto) {
        Vacation vacation = checkVacationByEmployee(employeeId, vacationId);

        Optional.ofNullable(vacationDto.startDate()).ifPresent(vacation::setStartDate);
        Optional.ofNullable(vacationDto.endDate()).ifPresent(vacation::setEndDate);
        Optional.of(vacationDto.isSickVacation()).ifPresent(vacation::setSickVacation);

        Vacation updatedVacation = vacationRepository.save(vacation);

        return VacationMapper.INSTANCE.vacationToVacationDtoResponse(updatedVacation);
    }

    @Override
    @Transactional
    public void deleteVacation(Long employeeId, Long vacationId) {
        Vacation vacation = checkVacationByEmployee(employeeId, vacationId);
        vacationRepository.delete(vacation);
    }

    private Vacation checkVacationByEmployee(Long employeeId, Long vacationId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(
                () -> new ResourceNotFoundException("Vacation", "id", vacationId.toString())
        );

        if(!vacation.getEmployee().getId().equals(employee.getId())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Vacation does not belong to employee");
        }

        return vacation;
    }
}
