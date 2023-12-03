package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.SupplierDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.dto.response.SupplierDtoResponse;
import com.ghopital.projet.entity.StockOrder;
import com.ghopital.projet.entity.Supplier;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.StockOrderMapper;
import com.ghopital.projet.mapper.SupplierMapper;
import com.ghopital.projet.repository.SupplierRepository;
import com.ghopital.projet.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierDtoResponse createSupplier(SupplierDtoRequest supplierDtoRequest) {
        // DTO to entity
        Supplier supplier = SupplierMapper.INSTANCE.supplierDtoRequestToSupplier(supplierDtoRequest);

        // Save supplier in database
        Supplier savedSupplier = supplierRepository.save(supplier);

        return SupplierMapper.INSTANCE.supplierToSupplierDtoResponse(savedSupplier);
    }

    @Override
    public SupplierDtoResponse getById(Long supplierId) {
        // Get supplier from database
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "id", supplierId.toString())
        );

        return SupplierMapper.INSTANCE.supplierToSupplierDtoResponse(supplier);
    }

    @Override
    public List<SupplierDtoResponse> getAllSupplier() {
        return supplierRepository.findAll().stream()
                .map(SupplierMapper.INSTANCE::supplierToSupplierDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockOrderDtoResponse> getStockOrdersForSupplier(Long supplierId) {
        // Get supplier from database
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "id", supplierId.toString())
        );

        // Get Delivery notes from supplier
        Set<StockOrder> stockOrders = supplier.getDeliveryNotes();

        return stockOrders.stream()
                .map(StockOrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDtoResponse updateSupplier(Long supplierId, SupplierDtoRequest dto) {
        // Get supplier from database
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "id", supplierId.toString())
        );

        if(StringUtils.hasText(dto.name())) {
            supplier.setName(dto.name());
        }

        if(StringUtils.hasText(dto.address())) {
            supplier.setAddress(dto.address());
        }

        if(StringUtils.hasText(dto.phone())) {
            supplier.setPhone(dto.phone());
        }

        if(StringUtils.hasText(dto.email())) {
            supplier.setEmail(dto.email());
        }

        // Save supplier update in database
        Supplier updatedSupplier = supplierRepository.save(supplier);

        return SupplierMapper.INSTANCE.supplierToSupplierDtoResponse(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        // Get supplier from database
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", "id", supplierId.toString())
        );

        // Delete supplier from database
        supplierRepository.delete(supplier);
    }
}
