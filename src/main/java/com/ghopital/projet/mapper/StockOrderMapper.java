package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.StockOrderDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.entity.StockOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockOrderMapper {
    StockOrderMapper INSTANCE = Mappers.getMapper(StockOrderMapper.class);
    StockOrder toEntity(StockOrderDtoRequest request);
    StockOrderDtoResponse toDto(StockOrder stockOrder);
}
