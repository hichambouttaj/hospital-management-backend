package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    FileDtoResponse fileToFileDtoResponse(File file);
}
