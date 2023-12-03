package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.SupplierDtoRequest;
import com.ghopital.projet.dto.response.SupplierDtoResponse;
import com.ghopital.projet.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupplierMapper {
    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);
    Supplier supplierDtoRequestToSupplier(SupplierDtoRequest supplierDtoRequest);
    SupplierDtoResponse supplierToSupplierDtoResponse(Supplier supplier);
}
