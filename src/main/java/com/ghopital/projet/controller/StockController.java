package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.StockDtoRequest;
import com.ghopital.projet.dto.request.StockUpdateDtoRequest;
import com.ghopital.projet.dto.response.StockDtoResponse;
import com.ghopital.projet.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {
    private final StockService stockService;
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    @PostMapping
    public ResponseEntity<StockDtoResponse> createStock(@Valid @RequestBody StockDtoRequest stockDtoRequest) {
        StockDtoResponse response = stockService.createStock(stockDtoRequest);
        return ResponseEntity.created(URI.create("/stocks/" + response.id()))
                .body(response);
    }
    @GetMapping("/{stockId}")
    public ResponseEntity<StockDtoResponse> getStockById(@PathVariable(name = "stockId") Long stockId) {
        StockDtoResponse response = stockService.getById(stockId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/products/{productId}")
    public ResponseEntity<StockDtoResponse> getStockOfProduct(@PathVariable(name = "productId") Long id) {
        StockDtoResponse response = stockService.getStockByProduct(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<StockDtoResponse>> getAllStock() {
        List<StockDtoResponse> responses = stockService.getAllStock();
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{stockId}")
    public ResponseEntity<StockDtoResponse> updateStock(
            @PathVariable(name = "stockId") Long stockId,
            @Valid @RequestBody StockUpdateDtoRequest stockUpdateDtoRequest) {
        StockDtoResponse response = stockService.updateStock(stockId, stockUpdateDtoRequest);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{stockId}/locations/{locationId}")
    public ResponseEntity<StockDtoResponse> addLocationToStock(
            @PathVariable(name = "stockId") Long stockId,
            @PathVariable(name = "locationId") Long locationId) {
        StockDtoResponse response = stockService.addLocationToStock(stockId, locationId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{stockId}")
    public ResponseEntity<String> deleteStock(@PathVariable(name = "stockId") Long stockId) {
        stockService.deleteStock(stockId);
        return ResponseEntity.ok("Stock deleted successfully");
    }
}
