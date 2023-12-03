package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.SupplierDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.dto.response.SupplierDtoResponse;

import java.util.List;

public interface SupplierService {
    SupplierDtoResponse createSupplier(SupplierDtoRequest supplierDtoRequest);
    SupplierDtoResponse getById(Long supplierId);
    List<SupplierDtoResponse> getAllSupplier();
    List<StockOrderDtoResponse> getStockOrdersForSupplier(Long supplierId);
    SupplierDtoResponse updateSupplier(Long supplierId, SupplierDtoRequest supplierDtoRequest);
    void deleteSupplier(Long supplierId);
}
