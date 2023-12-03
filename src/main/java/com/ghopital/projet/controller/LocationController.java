package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.LocationDtoRequest;
import com.ghopital.projet.dto.response.LocationDtoResponse;
import com.ghopital.projet.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @PostMapping
    public ResponseEntity<LocationDtoResponse> createLocation(@Valid @RequestBody LocationDtoRequest locationDtoRequest) {
        LocationDtoResponse response = locationService.createLocation(locationDtoRequest);
        return ResponseEntity.created(URI.create("/locations/" + response.id()))
                .body(response);
    }
    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDtoResponse> getLocationById(@PathVariable(name = "locationId") Long locationId) {
        LocationDtoResponse response = locationService.getById(locationId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<LocationDtoResponse>> getAllLocation() {
        List<LocationDtoResponse> responses = locationService.getAllLocation();
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDtoResponse> updateLocation(
            @PathVariable(name = "locationId") Long locationId, @Valid @RequestBody LocationDtoRequest locationDtoRequest) {
        LocationDtoResponse response = locationService.updateLocation(locationId, locationDtoRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable(name = "locationId") Long locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.ok("Location deleted successfully");
    }
}
