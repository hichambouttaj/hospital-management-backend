package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.StockDtoRequest;
import com.ghopital.projet.dto.response.StockDtoResponse;
import com.ghopital.projet.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);
    Stock toEntity(StockDtoRequest stockDtoRequest);
    StockDtoResponse toDto(Stock stock);
}
