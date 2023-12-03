package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.LocationDtoRequest;
import com.ghopital.projet.dto.response.LocationDtoResponse;
import com.ghopital.projet.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);
    Location locationDtoRequestToLocation(LocationDtoRequest locationDtoRequest);

    LocationDtoResponse locationToLocationDtoResponse(Location location);
}
