package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.ProductOrderDtoRequest;
import com.ghopital.projet.dto.response.ProductOrderDtoResponse;

import java.util.List;

public interface ProductOrderService {
    ProductOrderDtoResponse createProductOrder(ProductOrderDtoRequest productOrderDtoRequest);

    ProductOrderDtoResponse getProductOrder(Long code);

    List<ProductOrderDtoResponse> getAllProductOrder();

    List<ProductOrderDtoResponse> getProductOrdersOfProduct(Long productId);

    ProductOrderDtoResponse updateProductOrder(Long code, ProductOrderDtoRequest productOrderDtoRequest);

    void deleteProductOrder(Long code);
}
