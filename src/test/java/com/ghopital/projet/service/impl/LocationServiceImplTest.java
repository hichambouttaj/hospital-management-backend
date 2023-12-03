package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.LocationDtoRequest;
import com.ghopital.projet.dto.response.LocationDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.LocationRepository;
import com.ghopital.projet.repository.StockRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LocationServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class LocationServiceImplTest {
    @MockBean
    private LocationRepository locationRepository;

    @Autowired
    private LocationServiceImpl locationServiceImpl;

    @MockBean
    private StockRepository stockRepository;

    /**
     * Method under test:
     * {@link LocationServiceImpl#createLocation(LocationDtoRequest)}
     */
    @Test
    void testCreateLocation() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location);
        LocationDtoResponse actualCreateLocationResult = locationServiceImpl.createLocation(new LocationDtoRequest("Name",
                "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude"));
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualCreateLocationResult.address());
        assertEquals("Latitude", actualCreateLocationResult.latitude());
        assertEquals("Longitude", actualCreateLocationResult.longitude());
        assertEquals("Name", actualCreateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualCreateLocationResult.description());
        assertEquals(1L, actualCreateLocationResult.id().longValue());
    }

    /**
     * Method under test:  {@link LocationServiceImpl#createLocation(LocationDtoRequest)}
     */
    @Test
    void testCreateLocation2() {
        when(locationRepository.save(Mockito.<Location>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class,
                () -> locationServiceImpl.createLocation(new LocationDtoRequest("Name",
                        "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude")));
        verify(locationRepository).save(Mockito.<Location>any());
    }

    /**
     * Method under test: {@link LocationServiceImpl#getById(Long)}
     */
    @Test
    void testGetById() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualById = locationServiceImpl.getById(1L);
        verify(locationRepository).findById(Mockito.<Long>any());
        assertEquals("42 Main St", actualById.address());
        assertEquals("Latitude", actualById.latitude());
        assertEquals("Longitude", actualById.longitude());
        assertEquals("Name", actualById.name());
        assertEquals("The characteristics of someone or something", actualById.description());
        assertEquals(1L, actualById.id().longValue());
    }

    /**
     * Method under test: {@link LocationServiceImpl#getById(Long)}
     */
    @Test
    void testGetById2() {
        Optional<Location> emptyResult = Optional.empty();
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> locationServiceImpl.getById(1L));
        verify(locationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link LocationServiceImpl#getById(Long)}
     */
    @Test
    void testGetById3() {
        when(locationRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Location", "Location", "42"));
        assertThrows(ResourceNotFoundException.class, () -> locationServiceImpl.getById(1L));
        verify(locationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link LocationServiceImpl#getAllLocation()}
     */
    @Test
    void testGetAllLocation() {
        when(locationRepository.findAll()).thenReturn(new ArrayList<>());
        List<LocationDtoResponse> actualAllLocation = locationServiceImpl.getAllLocation();
        verify(locationRepository).findAll();
        assertTrue(actualAllLocation.isEmpty());
    }

    /**
     * Method under test:  {@link LocationServiceImpl#getAllLocation()}
     */
    @Test
    void testGetAllLocation2() {
        when(locationRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> locationServiceImpl.getAllLocation());
        verify(locationRepository).findAll();
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualUpdateLocationResult = locationServiceImpl.updateLocation(1L, new LocationDtoRequest(
                "Name", "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude"));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualUpdateLocationResult.address());
        assertEquals("Latitude", actualUpdateLocationResult.latitude());
        assertEquals("Longitude", actualUpdateLocationResult.longitude());
        assertEquals("Name", actualUpdateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateLocationResult.description());
        assertEquals(1L, actualUpdateLocationResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation2() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);
        when(locationRepository.save(Mockito.<Location>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class,
                () -> locationServiceImpl.updateLocation(1L, new LocationDtoRequest("Name",
                        "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude")));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation3() {
        Optional<Location> emptyResult = Optional.empty();
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> locationServiceImpl.updateLocation(1L, new LocationDtoRequest("Name",
                        "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude")));
        verify(locationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation4() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualUpdateLocationResult = locationServiceImpl.updateLocation(1L, new LocationDtoRequest("",
                "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude"));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualUpdateLocationResult.address());
        assertEquals("Latitude", actualUpdateLocationResult.latitude());
        assertEquals("Longitude", actualUpdateLocationResult.longitude());
        assertEquals("Name", actualUpdateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateLocationResult.description());
        assertEquals(1L, actualUpdateLocationResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation5() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualUpdateLocationResult = locationServiceImpl.updateLocation(1L,
                new LocationDtoRequest("Name", "", "42 Main St", "Latitude", "Longitude"));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualUpdateLocationResult.address());
        assertEquals("Latitude", actualUpdateLocationResult.latitude());
        assertEquals("Longitude", actualUpdateLocationResult.longitude());
        assertEquals("Name", actualUpdateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateLocationResult.description());
        assertEquals(1L, actualUpdateLocationResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation6() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualUpdateLocationResult = locationServiceImpl.updateLocation(1L,
                new LocationDtoRequest("Name", "The characteristics of someone or something", "", "Latitude", "Longitude"));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualUpdateLocationResult.address());
        assertEquals("Latitude", actualUpdateLocationResult.latitude());
        assertEquals("Longitude", actualUpdateLocationResult.longitude());
        assertEquals("Name", actualUpdateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateLocationResult.description());
        assertEquals(1L, actualUpdateLocationResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation7() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualUpdateLocationResult = locationServiceImpl.updateLocation(1L,
                new LocationDtoRequest("Name", "The characteristics of someone or something", "42 Main St", "", "Longitude"));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualUpdateLocationResult.address());
        assertEquals("Latitude", actualUpdateLocationResult.latitude());
        assertEquals("Longitude", actualUpdateLocationResult.longitude());
        assertEquals("Name", actualUpdateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateLocationResult.description());
        assertEquals(1L, actualUpdateLocationResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link LocationServiceImpl#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation8() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        when(locationRepository.save(Mockito.<Location>any())).thenReturn(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocationDtoResponse actualUpdateLocationResult = locationServiceImpl.updateLocation(1L,
                new LocationDtoRequest("Name", "The characteristics of someone or something", "42 Main St", "Latitude", ""));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(locationRepository).save(Mockito.<Location>any());
        assertEquals("42 Main St", actualUpdateLocationResult.address());
        assertEquals("Latitude", actualUpdateLocationResult.latitude());
        assertEquals("Longitude", actualUpdateLocationResult.longitude());
        assertEquals("Name", actualUpdateLocationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateLocationResult.description());
        assertEquals(1L, actualUpdateLocationResult.id().longValue());
    }

    /**
     * Method under test: {@link LocationServiceImpl#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);
        doNothing().when(locationRepository).delete(Mockito.<Location>any());
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        locationServiceImpl.deleteLocation(1L);
        verify(locationRepository).delete(Mockito.<Location>any());
        verify(locationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link LocationServiceImpl#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation2() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(locationRepository)
                .delete(Mockito.<Location>any());
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> locationServiceImpl.deleteLocation(1L));
        verify(locationRepository).delete(Mockito.<Location>any());
        verify(locationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link LocationServiceImpl#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation3() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLocation(location);
        stock.setQuantity(1L);

        HashSet<Stock> stocks = new HashSet<>();
        stocks.add(stock);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(stocks);
        Optional<Location> ofResult = Optional.of(location2);
        doNothing().when(locationRepository).delete(Mockito.<Location>any());
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Location location3 = new Location();
        location3.setAddress("42 Main St");
        location3.setDescription("The characteristics of someone or something");
        location3.setId(1L);
        location3.setLatitude("Latitude");
        location3.setLongitude("Longitude");
        location3.setName("Name");
        location3.setStocks(new HashSet<>());

        Stock stock2 = new Stock();
        stock2.setId(1L);
        stock2.setLocation(location3);
        stock2.setQuantity(1L);
        when(stockRepository.save(Mockito.<Stock>any())).thenReturn(stock2);
        locationServiceImpl.deleteLocation(1L);
        verify(locationRepository).delete(Mockito.<Location>any());
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link LocationServiceImpl#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation4() {
        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLocation(location);
        stock.setQuantity(1L);

        Location location2 = new Location();
        location2.setAddress("17 High St");
        location2.setDescription("Description");
        location2.setId(2L);
        location2.setLatitude("42");
        location2.setLongitude("42");
        location2.setName("42");
        location2.setStocks(new HashSet<>());

        Stock stock2 = new Stock();
        stock2.setId(2L);
        stock2.setLocation(location2);
        stock2.setQuantity(0L);

        HashSet<Stock> stocks = new HashSet<>();
        stocks.add(stock2);
        stocks.add(stock);

        Location location3 = new Location();
        location3.setAddress("42 Main St");
        location3.setDescription("The characteristics of someone or something");
        location3.setId(1L);
        location3.setLatitude("Latitude");
        location3.setLongitude("Longitude");
        location3.setName("Name");
        location3.setStocks(stocks);
        Optional<Location> ofResult = Optional.of(location3);
        doNothing().when(locationRepository).delete(Mockito.<Location>any());
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Location location4 = new Location();
        location4.setAddress("42 Main St");
        location4.setDescription("The characteristics of someone or something");
        location4.setId(1L);
        location4.setLatitude("Latitude");
        location4.setLongitude("Longitude");
        location4.setName("Name");
        location4.setStocks(new HashSet<>());

        Stock stock3 = new Stock();
        stock3.setId(1L);
        stock3.setLocation(location4);
        stock3.setQuantity(1L);
        when(stockRepository.save(Mockito.<Stock>any())).thenReturn(stock3);
        locationServiceImpl.deleteLocation(1L);
        verify(locationRepository).delete(Mockito.<Location>any());
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(stockRepository, atLeast(1)).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link LocationServiceImpl#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation5() {
        Optional<Location> emptyResult = Optional.empty();
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> locationServiceImpl.deleteLocation(1L));
        verify(locationRepository).findById(Mockito.<Long>any());
    }
}
