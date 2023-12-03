package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.StockOrderDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.service.StockOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stockOrders")
public class StockOrderController {
    private final StockOrderService stockOrderService;
    public StockOrderController(StockOrderService stockOrderService) {
        this.stockOrderService = stockOrderService;
    }
    @PostMapping
    public ResponseEntity<StockOrderDtoResponse> createStockOrder(
            @Valid @RequestBody StockOrderDtoRequest stockOrderDtoRequest) {
        StockOrderDtoResponse response = stockOrderService.createStockOrder(stockOrderDtoRequest);
        return ResponseEntity.created(URI.create("/productOrders/" + response.code()))
                .body(response);
    }
    @GetMapping("/{code}")
    public ResponseEntity<StockOrderDtoResponse> getStockOrder(@PathVariable(name = "code") Long code) {
        StockOrderDtoResponse response = stockOrderService.getStockOrder(code);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<StockOrderDtoResponse>> getAllStockOrder() {
        List<StockOrderDtoResponse> responses = stockOrderService.getAllStockOrder();
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/products/{productId}")
    public ResponseEntity<List<StockOrderDtoResponse>> getStockOrdersOfProduct(
            @PathVariable(name = "productId") Long id) {
        List<StockOrderDtoResponse> responses = stockOrderService.getStockOrdersOfProduct(id);
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{code}")
    public ResponseEntity<StockOrderDtoResponse> updateStockOrder(
            @PathVariable(name = "code") Long code,
            @Valid @RequestBody StockOrderDtoRequest stockOrderDtoRequest) {
        StockOrderDtoResponse response = stockOrderService.updateStockOrder(code, stockOrderDtoRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteStockOrder(@PathVariable(name = "code") Long code) {
        stockOrderService.deleteStockOrder(code);
        return ResponseEntity.ok("Stock Order deleted successfully");
    }
}
