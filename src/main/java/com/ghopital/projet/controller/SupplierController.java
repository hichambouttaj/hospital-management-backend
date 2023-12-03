package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.SupplierDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.dto.response.SupplierDtoResponse;
import com.ghopital.projet.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @PostMapping
    public ResponseEntity<SupplierDtoResponse> createSupplier(@Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        SupplierDtoResponse response = supplierService.createSupplier(supplierDtoRequest);
        return ResponseEntity.created(URI.create("/suppliers/" + response.id()))
                .body(response);
    }
    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDtoResponse> getSupplierById(@PathVariable(name = "supplierId") Long supplierId) {
        SupplierDtoResponse response = supplierService.getById(supplierId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<SupplierDtoResponse>> getAllSupplier() {
        List<SupplierDtoResponse> responses = supplierService.getAllSupplier();
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{supplierId}/deliveryNotes")
    public ResponseEntity<List<StockOrderDtoResponse>> getStockOrdersForSupplier(
            @PathVariable(name = "supplierId") Long supplierId) {
        List<StockOrderDtoResponse> responses = supplierService.getStockOrdersForSupplier(supplierId);
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierDtoResponse> updateSupplier(
            @PathVariable(name = "supplierId") Long supplierId, @Valid @RequestBody SupplierDtoRequest supplierDtoRequest) {
        SupplierDtoResponse response = supplierService.updateSupplier(supplierId, supplierDtoRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<String> deleteSupplier(@PathVariable(name = "supplierId") Long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}
