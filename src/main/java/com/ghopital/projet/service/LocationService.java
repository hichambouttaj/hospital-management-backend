package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.LocationDtoRequest;
import com.ghopital.projet.dto.response.LocationDtoResponse;

import java.util.List;

public interface LocationService {
    LocationDtoResponse createLocation(LocationDtoRequest locationDtoRequest);
    LocationDtoResponse getById(Long locationId);
    List<LocationDtoResponse> getAllLocation();
    LocationDtoResponse updateLocation(Long locationId, LocationDtoRequest locationDtoRequest);
    void deleteLocation(Long locationId);
}
