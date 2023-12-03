package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.StockDtoRequest;
import com.ghopital.projet.dto.request.StockUpdateDtoRequest;
import com.ghopital.projet.dto.response.StockDtoResponse;

import java.util.List;

public interface StockService {
    StockDtoResponse createStock(StockDtoRequest stockDtoRequest);
    StockDtoResponse getById(Long stockId);
    StockDtoResponse getStockByProduct(Long productId);
    List<StockDtoResponse> getAllStock();
    StockDtoResponse updateStock(Long stockId, StockUpdateDtoRequest stockUpdateDtoRequest);
    StockDtoResponse addLocationToStock(Long stockId, Long locationId);
    void deleteStock(Long stockId);
}
