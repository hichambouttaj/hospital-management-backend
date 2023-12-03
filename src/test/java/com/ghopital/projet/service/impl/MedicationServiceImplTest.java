package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.MedicationDtoRequest;
import com.ghopital.projet.dto.response.MedicationDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Medication;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.MedicationRepository;

import java.math.BigDecimal;
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

@ContextConfiguration(classes = {MedicationServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class MedicationServiceImplTest {
    @MockBean
    private MedicationRepository medicationRepository;

    @Autowired
    private MedicationServiceImpl medicationServiceImpl;

    /**
     * Method under test:
     * {@link MedicationServiceImpl#createMedication(MedicationDtoRequest)}
     */
    @Test
    void testCreateMedication() {
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

        Medication medication = new Medication();
        medication.setDeliveryNotes(new HashSet<>());
        medication.setDescription("The characteristics of someone or something");
        medication.setId(1L);
        medication.setManufacturer("Manufacturer");
        medication.setName("Name");
        medication.setPrice(new BigDecimal("2.3"));
        medication.setStock(stock);
        when(medicationRepository.save(Mockito.<Medication>any())).thenReturn(medication);
        MedicationDtoResponse actualCreateMedicationResult = medicationServiceImpl
                .createMedication(new MedicationDtoRequest("Name", "The characteristics of someone or something",
                        "Manufacturer", new BigDecimal("2.3")));
        verify(medicationRepository).save(Mockito.<Medication>any());
        assertEquals("Manufacturer", actualCreateMedicationResult.manufacturer());
        assertEquals("Name", actualCreateMedicationResult.name());
        MedicationDtoResponse.StockDto stockResult = actualCreateMedicationResult.stock();
        MedicationDtoResponse.StockDto.LocationDto locationResult = stockResult.location();
        assertEquals("Name", locationResult.name());
        assertEquals("The characteristics of someone or something", actualCreateMedicationResult.description());
        assertEquals(1L, actualCreateMedicationResult.id().longValue());
        assertEquals(1L, stockResult.id().longValue());
        assertEquals(1L, stockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
        BigDecimal expectedPriceResult = new BigDecimal("2.3");
        assertEquals(expectedPriceResult, actualCreateMedicationResult.price());
    }

    /**
     * Method under test:  {@link MedicationServiceImpl#createMedication(MedicationDtoRequest)}
     */
    @Test
    void testCreateMedication2() {
        when(medicationRepository.save(Mockito.<Medication>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class,
                () -> medicationServiceImpl.createMedication(new MedicationDtoRequest("Name",
                        "The characteristics of someone or something", "Manufacturer", new BigDecimal("2.3"))));
        verify(medicationRepository).save(Mockito.<Medication>any());
    }

    /**
     * Method under test: {@link MedicationServiceImpl#getById(Long)}
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

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLocation(location);
        stock.setQuantity(1L);

        Medication medication = new Medication();
        medication.setDeliveryNotes(new HashSet<>());
        medication.setDescription("The characteristics of someone or something");
        medication.setId(1L);
        medication.setManufacturer("Manufacturer");
        medication.setName("Name");
        medication.setPrice(new BigDecimal("2.3"));
        medication.setStock(stock);
        Optional<Medication> ofResult = Optional.of(medication);
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        MedicationDtoResponse actualById = medicationServiceImpl.getById(1L);
        verify(medicationRepository).findById(Mockito.<Long>any());
        assertEquals("Manufacturer", actualById.manufacturer());
        assertEquals("Name", actualById.name());
        MedicationDtoResponse.StockDto stockResult = actualById.stock();
        MedicationDtoResponse.StockDto.LocationDto locationResult = stockResult.location();
        assertEquals("Name", locationResult.name());
        assertEquals("The characteristics of someone or something", actualById.description());
        assertEquals(1L, actualById.id().longValue());
        assertEquals(1L, stockResult.id().longValue());
        assertEquals(1L, stockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
        BigDecimal expectedPriceResult = new BigDecimal("2.3");
        assertEquals(expectedPriceResult, actualById.price());
    }

    /**
     * Method under test: {@link MedicationServiceImpl#getById(Long)}
     */
    @Test
    void testGetById2() {
        Optional<Medication> emptyResult = Optional.empty();
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> medicationServiceImpl.getById(1L));
        verify(medicationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link MedicationServiceImpl#getById(Long)}
     */
    @Test
    void testGetById3() {
        when(medicationRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Medication", "Medication", "42"));
        assertThrows(ResourceNotFoundException.class, () -> medicationServiceImpl.getById(1L));
        verify(medicationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link MedicationServiceImpl#getAllMedication()}
     */
    @Test
    void testGetAllMedication() {
        when(medicationRepository.findAll()).thenReturn(new ArrayList<>());
        List<MedicationDtoResponse> actualAllMedication = medicationServiceImpl.getAllMedication();
        verify(medicationRepository).findAll();
        assertTrue(actualAllMedication.isEmpty());
    }

    /**
     * Method under test:  {@link MedicationServiceImpl#getAllMedication()}
     */
    @Test
    void testGetAllMedication2() {
        when(medicationRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> medicationServiceImpl.getAllMedication());
        verify(medicationRepository).findAll();
    }

    /**
     * Method under test:
     * {@link MedicationServiceImpl#updateMedication(Long, MedicationDtoRequest)}
     */
    @Test
    void testUpdateMedication() {
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

        Medication medication = new Medication();
        medication.setDeliveryNotes(new HashSet<>());
        medication.setDescription("The characteristics of someone or something");
        medication.setId(1L);
        medication.setManufacturer("Manufacturer");
        medication.setName("Name");
        medication.setPrice(new BigDecimal("2.3"));
        medication.setStock(stock);
        Optional<Medication> ofResult = Optional.of(medication);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());

        Stock stock2 = new Stock();
        stock2.setId(1L);
        stock2.setLocation(location2);
        stock2.setQuantity(1L);

        Medication medication2 = new Medication();
        medication2.setDeliveryNotes(new HashSet<>());
        medication2.setDescription("The characteristics of someone or something");
        medication2.setId(1L);
        medication2.setManufacturer("Manufacturer");
        medication2.setName("Name");
        medication2.setPrice(new BigDecimal("2.3"));
        medication2.setStock(stock2);
        when(medicationRepository.save(Mockito.<Medication>any())).thenReturn(medication2);
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        MedicationDtoResponse actualUpdateMedicationResult = medicationServiceImpl.updateMedication(1L,
                new MedicationDtoRequest("Name", "The characteristics of someone or something", "Manufacturer",
                        new BigDecimal("2.3")));
        verify(medicationRepository).findById(Mockito.<Long>any());
        verify(medicationRepository).save(Mockito.<Medication>any());
        assertEquals("Manufacturer", actualUpdateMedicationResult.manufacturer());
        assertEquals("Name", actualUpdateMedicationResult.name());
        MedicationDtoResponse.StockDto stockResult = actualUpdateMedicationResult.stock();
        MedicationDtoResponse.StockDto.LocationDto locationResult = stockResult.location();
        assertEquals("Name", locationResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateMedicationResult.description());
        assertEquals(1L, actualUpdateMedicationResult.id().longValue());
        assertEquals(1L, stockResult.id().longValue());
        assertEquals(1L, stockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
        BigDecimal expectedPriceResult = new BigDecimal("2.3");
        assertEquals(expectedPriceResult, actualUpdateMedicationResult.price());
    }

    /**
     * Method under test:
     * {@link MedicationServiceImpl#updateMedication(Long, MedicationDtoRequest)}
     */
    @Test
    void testUpdateMedication2() {
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

        Medication medication = new Medication();
        medication.setDeliveryNotes(new HashSet<>());
        medication.setDescription("The characteristics of someone or something");
        medication.setId(1L);
        medication.setManufacturer("Manufacturer");
        medication.setName("Name");
        medication.setPrice(new BigDecimal("2.3"));
        medication.setStock(stock);
        Optional<Medication> ofResult = Optional.of(medication);
        when(medicationRepository.save(Mockito.<Medication>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class,
                () -> medicationServiceImpl.updateMedication(1L, new MedicationDtoRequest("Name",
                        "The characteristics of someone or something", "Manufacturer", new BigDecimal("2.3"))));
        verify(medicationRepository).findById(Mockito.<Long>any());
        verify(medicationRepository).save(Mockito.<Medication>any());
    }

    /**
     * Method under test:
     * {@link MedicationServiceImpl#updateMedication(Long, MedicationDtoRequest)}
     */
    @Test
    void testUpdateMedication3() {
        Optional<Medication> emptyResult = Optional.empty();
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> medicationServiceImpl.updateMedication(1L, new MedicationDtoRequest("Name",
                        "The characteristics of someone or something", "Manufacturer", new BigDecimal("2.3"))));
        verify(medicationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MedicationServiceImpl#deleteMedication(Long)}
     */
    @Test
    void testDeleteMedication() {
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

        Medication medication = new Medication();
        medication.setDeliveryNotes(new HashSet<>());
        medication.setDescription("The characteristics of someone or something");
        medication.setId(1L);
        medication.setManufacturer("Manufacturer");
        medication.setName("Name");
        medication.setPrice(new BigDecimal("2.3"));
        medication.setStock(stock);
        Optional<Medication> ofResult = Optional.of(medication);
        doNothing().when(medicationRepository).delete(Mockito.<Medication>any());
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        medicationServiceImpl.deleteMedication(1L);
        verify(medicationRepository).delete(Mockito.<Medication>any());
        verify(medicationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MedicationServiceImpl#deleteMedication(Long)}
     */
    @Test
    void testDeleteMedication2() {
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

        Medication medication = new Medication();
        medication.setDeliveryNotes(new HashSet<>());
        medication.setDescription("The characteristics of someone or something");
        medication.setId(1L);
        medication.setManufacturer("Manufacturer");
        medication.setName("Name");
        medication.setPrice(new BigDecimal("2.3"));
        medication.setStock(stock);
        Optional<Medication> ofResult = Optional.of(medication);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(medicationRepository)
                .delete(Mockito.<Medication>any());
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> medicationServiceImpl.deleteMedication(1L));
        verify(medicationRepository).delete(Mockito.<Medication>any());
        verify(medicationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MedicationServiceImpl#deleteMedication(Long)}
     */
    @Test
    void testDeleteMedication3() {
        Optional<Medication> emptyResult = Optional.empty();
        when(medicationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> medicationServiceImpl.deleteMedication(1L));
        verify(medicationRepository).findById(Mockito.<Long>any());
    }
}
