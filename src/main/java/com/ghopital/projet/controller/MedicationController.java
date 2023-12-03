package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.MedicationDtoRequest;
import com.ghopital.projet.dto.response.MedicationDtoResponse;
import com.ghopital.projet.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
public class MedicationController {
    private final MedicationService medicationService;
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }
    @PostMapping
    public ResponseEntity<MedicationDtoResponse> createMedication(@Valid @RequestBody MedicationDtoRequest request) {
        MedicationDtoResponse response = medicationService.createMedication(request);
        return ResponseEntity.created(URI.create("/medications/" + response.id()))
                .body(response);
    }
    @GetMapping("/{medicationId}")
    public ResponseEntity<MedicationDtoResponse> getMedicationById(@PathVariable(name = "medicationId") Long medicationId) {
        MedicationDtoResponse response = medicationService.getById(medicationId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<MedicationDtoResponse>> getAllMedication() {
        List<MedicationDtoResponse> responses = medicationService.getAllMedication();
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{medicationId}")
    public ResponseEntity<MedicationDtoResponse> updateMedication(
            @PathVariable(name = "medicationId") Long medicationId,
            @Valid @RequestBody MedicationDtoRequest request) {
        MedicationDtoResponse response = medicationService.updateMedication(medicationId, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<String> deleteMedication(
            @PathVariable(name = "medicationId") Long medicationId) {
        medicationService.deleteMedication(medicationId);
        return ResponseEntity.ok("Medication deleted successfully");
    }
}
