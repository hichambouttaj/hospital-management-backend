package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.MaterialDtoRequest;
import com.ghopital.projet.dto.response.MaterialCreateDtoResponse;
import com.ghopital.projet.dto.response.MaterialDtoResponse;
import com.ghopital.projet.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {
    private final MaterialService materialService;
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }
    @PostMapping
    public ResponseEntity<MaterialCreateDtoResponse> createMaterial(@Valid @RequestBody MaterialDtoRequest materialDtoRequest) {
        MaterialCreateDtoResponse response = materialService.createMaterial(materialDtoRequest);
        return ResponseEntity.created(URI.create("/materials/" + response.id()))
                .body(response);
    }
    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialDtoResponse> getMaterialById(@PathVariable(name = "materialId") Long materialId) {
        MaterialDtoResponse response = materialService.getById(materialId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<MaterialDtoResponse>> getAllMaterial() {
        List<MaterialDtoResponse> responses = materialService.getAllMaterial();
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{materialId}")
    public ResponseEntity<MaterialCreateDtoResponse> updateMaterial(
            @PathVariable(name = "materialId") Long materialId, @Valid @RequestBody MaterialDtoRequest materialDtoRequest) {
        MaterialCreateDtoResponse response = materialService.updateMaterial(materialId, materialDtoRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteMaterial(@PathVariable(name = "materialId") Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.ok("Material deleted successfully");
    }
}
