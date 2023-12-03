package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.DiplomaDtoRequest;
import com.ghopital.projet.dto.response.DiplomaDtoResponse;
import com.ghopital.projet.service.DiplomaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees/{employeeId}/diplomas")
public class DiplomaController {
    private final DiplomaService diplomaService;
    public DiplomaController(DiplomaService diplomaService) {
        this.diplomaService = diplomaService;
    }
    @PostMapping
    public ResponseEntity<DiplomaDtoResponse> createDiploma(
            @PathVariable(name = "employeeId") Long employeeId, @Valid @RequestBody DiplomaDtoRequest diplomaDto) {
        DiplomaDtoResponse response = diplomaService.createDiploma(employeeId, diplomaDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{diplomaId}")
    public ResponseEntity<DiplomaDtoResponse> getDiplomaById(
            @PathVariable(name = "employeeId") Long employeeId, @PathVariable(name = "diplomaId") Long diplomaId) {
        DiplomaDtoResponse response = diplomaService.getDiplomaById(employeeId, diplomaId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<DiplomaDtoResponse>> getDiplomasForEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(diplomaService.getEmployeeDiplomas(employeeId));
    }
    @PutMapping("/{diplomaId}")
    public ResponseEntity<DiplomaDtoResponse> updateDiploma(
            @PathVariable(name = "employeeId") Long employeeId,
            @PathVariable(name = "diplomaId") Long diplomaId,
            @Valid @RequestBody DiplomaDtoRequest diplomaDto) {
        DiplomaDtoResponse response = diplomaService.updateDiploma(employeeId, diplomaId, diplomaDto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{diplomaId}/document/{documentId}")
    public ResponseEntity<DiplomaDtoResponse> addDocumentToDiploma(
            @PathVariable(name = "employeeId") Long employeeId,
            @PathVariable(name = "diplomaId") Long diplomaId,
            @PathVariable(name = "documentId") Long documentId) {
        DiplomaDtoResponse response = diplomaService.addDocumentToDiploma(employeeId, diplomaId, documentId);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{diplomaId}")
    public ResponseEntity<String> deleteDiploma(
            @PathVariable(name = "employeeId") Long employeeId, @PathVariable(name = "diplomaId") Long diplomaId) {
        diplomaService.deleteDiploma(employeeId, diplomaId);
        return ResponseEntity.ok("Diploma deleted successfully");
    }
}
