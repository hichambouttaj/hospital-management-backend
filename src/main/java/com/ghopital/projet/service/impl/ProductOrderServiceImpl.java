package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.ProductOrderDtoRequest;
import com.ghopital.projet.dto.response.ProductOrderDtoResponse;
import com.ghopital.projet.entity.*;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.ProductOrderMapper;
import com.ghopital.projet.repository.*;
import com.ghopital.projet.service.ProductOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {
    private final ProductOrderRepository productOrderRepository;
    private final ServiceRepository serviceRepository;
    private final DeliveryNoteRepository deliveryNoteRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository, ServiceRepository serviceRepository, DeliveryNoteRepository deliveryNoteRepository, StockRepository stockRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.productOrderRepository = productOrderRepository;
        this.serviceRepository = serviceRepository;
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    @Override
    @Transactional
    public ProductOrderDtoResponse createProductOrder(ProductOrderDtoRequest productOrderDtoRequest) {
        // Dto to entity
        ProductOrder productOrder = ProductOrderMapper.INSTANCE.toEntity(productOrderDtoRequest);

        // Get hospital service from db
        HospitalService hospitalService = serviceRepository.findByName(productOrderDtoRequest.hospitalServiceName())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital service", "id", productOrderDtoRequest.hospitalServiceName()));

        // Add hospital service to order
        productOrder.setHospitalService(hospitalService);

        // Add order to db
        ProductOrder savedProductOrder = productOrderRepository.save(productOrder);

        // Get list of delivery note
        Set<ProductOrderDtoRequest.DeliveryNoteDto> deliveryNotes = productOrderDtoRequest.deliveryNotes();

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

                    Long previousQuantity = stock.getQuantity();
                    long diff = previousQuantity - dn.quantity();

                    if(diff < 0) {
                        throw new AppException(HttpStatus.BAD_REQUEST, String.format("Not enough quantity in stock of product with id %d", product.getId()));
                    }

                    stock.setQuantity(previousQuantity - dn.quantity());

                    // Save stock update in db
                    stockRepository.save(stock);

                    // Save delivery note in db
                    DeliveryNote deliveryNote = new DeliveryNote();
                    deliveryNote.setProduct(product);
                    deliveryNote.setQuantity(dn.quantity());
                    deliveryNote.setOrder(savedProductOrder);
                    deliveryNoteRepository.save(deliveryNote);
                }
        );

        // Save update
        ProductOrder order = productOrderRepository.save(savedProductOrder);

        return ProductOrderMapper.INSTANCE.toDto(order);
    }

    @Override
    public ProductOrderDtoResponse getProductOrder(Long code) {
        // Get order by code
        ProductOrder productOrder = productOrderRepository.findById(code).orElseThrow(
                () -> new ResourceNotFoundException("Product Order", "code", code.toString()));

        return ProductOrderMapper.INSTANCE.toDto(productOrder);
    }

    @Override
    public List<ProductOrderDtoResponse> getAllProductOrder() {
        // Get orders form db
        return productOrderRepository.findAll().stream()
                .map(ProductOrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductOrderDtoResponse> getProductOrdersOfProduct(Long productId) {
        // Get product from db
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId.toString()));

         return productOrderRepository.findAllByProduct(productId).stream()
                .peek(po -> po.setDeliveryNotes(po.getDeliveryNotes().stream().filter(
                        dn -> dn.getProduct().getId().equals(productId)).collect(Collectors.toSet())))
                .map(ProductOrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductOrderDtoResponse updateProductOrder(Long code, ProductOrderDtoRequest productOrderDtoRequest) {
        // Get order from database
        ProductOrder productOrder = productOrderRepository.findById(code).orElseThrow(
                () -> new ResourceNotFoundException("Product order", "code", code.toString()));

        if(StringUtils.hasText(productOrderDtoRequest.hospitalServiceName())) {
            // Get hospital service from db
            HospitalService hospitalService = serviceRepository.findByName(productOrderDtoRequest.hospitalServiceName())
                    .orElseThrow(() -> new ResourceNotFoundException("Hospital service", "id", productOrderDtoRequest.hospitalServiceName()));

            // Add hospital service to order
            productOrder.setHospitalService(hospitalService);
        }

        // Get list of delivery note
        Set<ProductOrderDtoRequest.DeliveryNoteDto> deliveryNotes = productOrderDtoRequest.deliveryNotes();

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
                    deliveryNote.setOrder(productOrder);
                    deliveryNoteRepository.save(deliveryNote);
                }
        );

        // Save update
        ProductOrder order = productOrderRepository.save(productOrder);

        return ProductOrderMapper.INSTANCE.toDto(order);
    }

    @Override
    public void deleteProductOrder(Long code) {
        // Get order by code
        ProductOrder productOrder = productOrderRepository.findById(code).orElseThrow(
                () -> new ResourceNotFoundException("Product Order", "code", code.toString()));

        // Delete order from database
        productOrderRepository.delete(productOrder);
    }
}
