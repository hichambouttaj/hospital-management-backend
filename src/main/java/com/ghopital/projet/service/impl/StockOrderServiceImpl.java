package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.StockOrderDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.entity.*;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.StockOrderMapper;
import com.ghopital.projet.repository.*;
import com.ghopital.projet.service.StockOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StockOrderServiceImpl implements StockOrderService {
    private final StockOrderRepository stockOrderRepository;
    private final SupplierRepository supplierRepository;
    private final DeliveryNoteRepository deliveryNoteRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public StockOrderServiceImpl(StockOrderRepository stockOrderRepository, SupplierRepository supplierRepository, DeliveryNoteRepository deliveryNoteRepository, StockRepository stockRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.stockOrderRepository = stockOrderRepository;
        this.supplierRepository = supplierRepository;
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public StockOrderDtoResponse createStockOrder(StockOrderDtoRequest stockOrderDtoRequest) {
        // Dto to entity
        StockOrder stockOrder = StockOrderMapper.INSTANCE.toEntity(stockOrderDtoRequest);

        // Get supplier from db
        Supplier supplier = supplierRepository.findById(stockOrderDtoRequest.supplierId()).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "id", stockOrderDtoRequest.supplierId().toString()));

        // Add supplier to stock order
        stockOrder.setSupplier(supplier);

        // Save order in db
        StockOrder savedStockOrder = stockOrderRepository.save(stockOrder);

        // Get list of delivery note
        Set<StockOrderDtoRequest.DeliveryNoteDto> deliveryNotes = stockOrderDtoRequest.deliveryNotes();

        deliveryNotes.forEach(
                dn -> {
                    // Get product from db
                    Product product = productRepository.findById(dn.productId()).orElseThrow(
                            () -> new ResourceNotFoundException("Product", "id", dn.productId().toString())
                    );

                    // Get product stock
                    Stock stock = product.getStock();
                    if(stock == null) {
                        throw new AppException(HttpStatus.BAD_REQUEST, String.format("Product with id %d don't have stock", product.getId()));
                    }

                    Long previousQuantity = stock.getQuantity();

                    stock.setQuantity(previousQuantity + dn.quantity());

                    // Save stock update in db
                    stockRepository.save(stock);

                    // Save delivery note in db
                    DeliveryNote deliveryNote = new DeliveryNote();
                    deliveryNote.setProduct(product);
                    deliveryNote.setQuantity(dn.quantity());
                    deliveryNote.setOrder(savedStockOrder);
                    deliveryNoteRepository.save(deliveryNote);
                }
        );


        // Save update
        StockOrder order = stockOrderRepository.save(savedStockOrder);

        return StockOrderMapper.INSTANCE.toDto(order);
    }

    @Override
    public StockOrderDtoResponse getStockOrder(Long code) {
        // Get order by code
        StockOrder stockOrder = stockOrderRepository.findById(code).orElseThrow(
                () -> new ResourceNotFoundException("Stock Order", "code", code.toString()));

        return StockOrderMapper.INSTANCE.toDto(stockOrder);
    }

    @Override
    public List<StockOrderDtoResponse> getAllStockOrder() {
        // Get orders form db
        return stockOrderRepository.findAll().stream()
                .map(StockOrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockOrderDtoResponse> getStockOrdersOfProduct(Long id) {
        // Get product from db
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString()));

        return stockOrderRepository.findAllByProduct(id).stream()
                .peek(so -> so.setDeliveryNotes(so.getDeliveryNotes().stream().filter(
                        dn -> dn.getProduct().getId().equals(id)).collect(Collectors.toSet())))
                .map(StockOrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockOrderDtoResponse updateStockOrder(Long code, StockOrderDtoRequest stockOrderDtoRequest) {
        // Get order from database
        StockOrder stockOrder = stockOrderRepository.findById(code).orElseThrow(
                () -> new ResourceNotFoundException("Stock order", "code", code.toString()));

        if(StringUtils.hasText(stockOrderDtoRequest.deliveryLocation())) {
            stockOrder.setDeliveryLocation(stockOrderDtoRequest.deliveryLocation());
        }

        // Get supplier from db
        Supplier supplier = supplierRepository.findById(stockOrderDtoRequest.supplierId()).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "id", stockOrderDtoRequest.supplierId().toString()));

        // Add supplier to stock order
        stockOrder.setSupplier(supplier);

        Set<StockOrderDtoRequest.DeliveryNoteDto> deliveryNotes = stockOrderDtoRequest.deliveryNotes();

        deliveryNotes.forEach(
                dn -> {
                    // Get product from db
                    Product product = productRepository.findById(dn.productId()).orElseThrow(
                            () -> new ResourceNotFoundException("Product", "id", dn.productId().toString())
                    );

                    // Get product stock
                    Stock stock = product.getStock();
                    if(stock == null) {
                        throw new AppException(HttpStatus.BAD_REQUEST, String.format("Product with id %d don't have stock.", product.getId()));
                    }

                    stock.setQuantity(dn.quantity());

                    // Save stock update in db
                    stockRepository.save(stock);

                    // Save delivery note in db
                    DeliveryNote deliveryNote = new DeliveryNote();
                    deliveryNote.setProduct(product);
                    deliveryNote.setQuantity(dn.quantity());
                    deliveryNote.setOrder(stockOrder);
                    deliveryNoteRepository.save(deliveryNote);
                }
        );

        // Save update
        StockOrder order = stockOrderRepository.save(stockOrder);

        return StockOrderMapper.INSTANCE.toDto(order);
    }

    @Override
    public void deleteStockOrder(Long code) {
        // Get order by code
        StockOrder stockOrder = stockOrderRepository.findById(code).orElseThrow(
                () -> new ResourceNotFoundException("Stock Order", "code", code.toString()));

        // Delete order from database
        stockOrderRepository.delete(stockOrder);
    }
}
