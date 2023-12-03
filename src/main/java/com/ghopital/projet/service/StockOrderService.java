package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.StockOrderDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;

import java.util.List;

public interface StockOrderService {
    StockOrderDtoResponse createStockOrder(StockOrderDtoRequest stockOrderDtoRequest);
    StockOrderDtoResponse getStockOrder(Long code);
    List<StockOrderDtoResponse> getAllStockOrder();
    List<StockOrderDtoResponse> getStockOrdersOfProduct(Long id);
    StockOrderDtoResponse updateStockOrder(Long code, StockOrderDtoRequest stockOrderDtoRequest);
    void deleteStockOrder(Long code);
}
