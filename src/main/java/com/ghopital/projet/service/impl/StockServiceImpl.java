package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.StockDtoRequest;
import com.ghopital.projet.dto.request.StockUpdateDtoRequest;
import com.ghopital.projet.dto.response.StockDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Product;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.StockMapper;
import com.ghopital.projet.repository.LocationRepository;
import com.ghopital.projet.repository.ProductRepository;
import com.ghopital.projet.repository.StockRepository;
import com.ghopital.projet.service.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    public StockServiceImpl(StockRepository stockRepository, ProductRepository productRepository, LocationRepository locationRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
    }
    @Override
    public StockDtoResponse createStock(StockDtoRequest stockDtoRequest) {
        // Get product from db
        Product product = productRepository.findById(stockDtoRequest.productId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", stockDtoRequest.productId().toString()));

        // DTO to entity
        Stock stock = StockMapper.INSTANCE.toEntity(stockDtoRequest);

        // Add product to stock
        stock.setProduct(product);

        // Save stock in database
        Stock savedStock = stockRepository.save(stock);

        return StockMapper.INSTANCE.toDto(savedStock);
    }

    @Override
    public StockDtoResponse getById(Long stockId) {
        // Get stock from database
        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new ResourceNotFoundException("Stock", "id", stockId.toString())
        );

        return StockMapper.INSTANCE.toDto(stock);
    }

    @Override
    public StockDtoResponse getStockByProduct(Long productId) {
        // Get product from db
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId.toString()));

        Stock stock = product.getStock();

        return StockMapper.INSTANCE.toDto(stock);
    }

    @Override
    public List<StockDtoResponse> getAllStock() {
        return stockRepository.findAll().stream()
                .map(StockMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public StockDtoResponse updateStock(Long stockId, StockUpdateDtoRequest dto) {
        // Get stock from database
        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new ResourceNotFoundException("Stock", "id", stockId.toString())
        );

        stock.setQuantity(dto.quantity());

        // Save stock update in database
        Stock updatedStock = stockRepository.save(stock);

        return StockMapper.INSTANCE.toDto(updatedStock);
    }

    @Override
    public StockDtoResponse addLocationToStock(Long stockId, Long locationId) {
        // Get stock from database
        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new ResourceNotFoundException("Stock", "id", stockId.toString())
        );

        // Get location from database
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", locationId.toString())
        );

        // Add location to stock
        stock.setLocation(location);

        // Save stock update in database
        Stock updatedStock = stockRepository.save(stock);

        return StockMapper.INSTANCE.toDto(updatedStock);
    }

    @Override
    public void deleteStock(Long stockId) {
        // Get stock from database
        Stock stock = stockRepository.findById(stockId).orElseThrow(
                () -> new ResourceNotFoundException("Stock", "id", stockId.toString())
        );

        // Delete stock from database
        stockRepository.delete(stock);
    }
}
