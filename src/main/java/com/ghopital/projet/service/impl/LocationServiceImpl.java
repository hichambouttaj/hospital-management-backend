package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.LocationDtoRequest;
import com.ghopital.projet.dto.response.LocationDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.LocationMapper;
import com.ghopital.projet.repository.LocationRepository;
import com.ghopital.projet.repository.StockRepository;
import com.ghopital.projet.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    public LocationServiceImpl(LocationRepository locationRepository, StockRepository stockRepository) {
        this.locationRepository = locationRepository;
        this.stockRepository = stockRepository;
    }
    @Override
    public LocationDtoResponse createLocation(LocationDtoRequest locationDtoRequest) {
        // DTO to entity
        Location location = LocationMapper.INSTANCE.locationDtoRequestToLocation(locationDtoRequest);

        // Save location in database
        Location savedLocation = locationRepository.save(location);

        return LocationMapper.INSTANCE.locationToLocationDtoResponse(savedLocation);
    }

    @Override
    public LocationDtoResponse getById(Long locationId) {
        // Get location from database
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", locationId.toString())
        );

        return LocationMapper.INSTANCE.locationToLocationDtoResponse(location);
    }

    @Override
    public List<LocationDtoResponse> getAllLocation() {
        // Get list of location from database
        return locationRepository.findAll().stream()
                .map(LocationMapper.INSTANCE::locationToLocationDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDtoResponse updateLocation(Long locationId, LocationDtoRequest dto) {
        // Get location from database
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", locationId.toString())
        );

        if(StringUtils.hasText(dto.name()))
            location.setName(dto.name());

        if(StringUtils.hasText(dto.description()))
            location.setName(dto.name());

        if(StringUtils.hasText(dto.address()))
            location.setAddress(dto.address());

        if(StringUtils.hasText(dto.latitude()))
            location.setLatitude(dto.latitude());

        if(StringUtils.hasText(dto.longitude()))
            location.setLongitude(dto.longitude());

        // Save location update in database
        Location updatedLocation = locationRepository.save(location);

        return LocationMapper.INSTANCE.locationToLocationDtoResponse(updatedLocation);
    }

    @Override
    @Transactional
    public void deleteLocation(Long locationId) {
        // Get location from database
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new ResourceNotFoundException("Location", "id", locationId.toString())
        );

        // Get stock from location
        Set<Stock> stocks = location.getStocks();

        // Remove location from stock
        stocks.forEach(
                s -> {
                    if(s != null) {
                        s.setLocation(null);
                        stockRepository.save(s);
                    }
                }
        );

        // Remove location from database
        locationRepository.delete(location);
    }
}
