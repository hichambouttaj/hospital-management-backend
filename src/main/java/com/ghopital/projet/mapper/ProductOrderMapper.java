package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.ProductOrderDtoRequest;
import com.ghopital.projet.dto.response.ProductOrderDtoResponse;
import com.ghopital.projet.entity.ProductOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductOrderMapper {
    ProductOrderMapper INSTANCE = Mappers.getMapper(ProductOrderMapper.class);
    ProductOrder toEntity(ProductOrderDtoRequest request);
    @Mappings({
            @Mapping(target = "hospitalServiceName", source = "productOrder.hospitalService.name")
    })
    ProductOrderDtoResponse toDto(ProductOrder productOrder);

}
