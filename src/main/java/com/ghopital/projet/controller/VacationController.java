package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.VacationDtoRequest;
import com.ghopital.projet.dto.response.VacationDtoResponse;
import com.ghopital.projet.service.VacationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees/{employeeId}/vacations")
public class VacationController {
    private final VacationService vacationService;
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }
    @PostMapping
    public ResponseEntity<VacationDtoResponse> createVacation(
            @PathVariable(name = "employeeId") Long employeeId, @Valid @RequestBody VacationDtoRequest vacationDto) {
        VacationDtoResponse response = vacationService.createVacation(employeeId, vacationDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{vacationId}")
    public ResponseEntity<VacationDtoResponse> getVacationById(
            @PathVariable(name = "employeeId") Long employeeId, @PathVariable(name = "vacationId") Long vacationId) {
        VacationDtoResponse response = vacationService.getVacationById(employeeId, vacationId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<VacationDtoResponse>> getVacationsForEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(vacationService.getEmployeeVacations(employeeId));
    }
    @PutMapping("/{vacationId}")
    public ResponseEntity<VacationDtoResponse> updateVacation(
            @PathVariable(name = "employeeId") Long employeeId,
            @PathVariable(name = "vacationId") Long vacationId,
            @RequestBody VacationDtoRequest vacationDto) {
        VacationDtoResponse response = vacationService.updateVacation(employeeId, vacationId, vacationDto);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{vacationId}")
    public ResponseEntity<String> deleteVacation(
            @PathVariable(name = "employeeId") Long employeeId, @PathVariable(name = "vacationId") Long vacationId) {
        vacationService.deleteVacation(employeeId, vacationId);
        return ResponseEntity.ok("Vacation deleted successfully");
    }
}
