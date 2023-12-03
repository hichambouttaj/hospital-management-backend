package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.ProductOrderDtoRequest;
import com.ghopital.projet.dto.response.ProductOrderDtoResponse;
import com.ghopital.projet.service.ProductOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productOrders")
public class ProductOrderController {
    private final ProductOrderService productOrderService;
    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }
    @PostMapping
    public ResponseEntity<ProductOrderDtoResponse> createProductOrder(
            @Valid @RequestBody ProductOrderDtoRequest productOrderDtoRequest) {
        ProductOrderDtoResponse response = productOrderService.createProductOrder(productOrderDtoRequest);
        return ResponseEntity.created(URI.create("/productOrders/" + response.code()))
                .body(response);
    }
    @GetMapping("/{code}")
    public ResponseEntity<ProductOrderDtoResponse> getProductOrder(@PathVariable(name = "code") Long code) {
        ProductOrderDtoResponse response = productOrderService.getProductOrder(code);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<ProductOrderDtoResponse>> getAllProductOrder() {
        List<ProductOrderDtoResponse> responses = productOrderService.getAllProductOrder();
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/products/{productId}")
    public ResponseEntity<List<ProductOrderDtoResponse>> getProductOrdersOfProduct(
            @PathVariable(name = "productId") Long id) {
        List<ProductOrderDtoResponse> responses = productOrderService.getProductOrdersOfProduct(id);
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{code}")
    public ResponseEntity<ProductOrderDtoResponse> updateProductOrder(
            @PathVariable(name = "code") Long code,
            @Valid @RequestBody ProductOrderDtoRequest productOrderDtoRequest) {
        ProductOrderDtoResponse response = productOrderService.updateProductOrder(code, productOrderDtoRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteProductOrder(@PathVariable(name = "code") Long code) {
        productOrderService.deleteProductOrder(code);
        return ResponseEntity.ok("Product Order deleted successfully");
    }
}
