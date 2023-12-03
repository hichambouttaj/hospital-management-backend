package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.MaterialDtoRequest;
import com.ghopital.projet.dto.response.MaterialCreateDtoResponse;
import com.ghopital.projet.dto.response.MaterialDtoResponse;
import com.ghopital.projet.entity.Material;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaterialMapper {
    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);
    Material materialDtoRequestToMaterial(MaterialDtoRequest materialDtoRequest);
    MaterialDtoResponse materialToMaterialDtoResponse(Material material);
    MaterialCreateDtoResponse materialToMaterialCreateDtoResponse(Material material);
}
