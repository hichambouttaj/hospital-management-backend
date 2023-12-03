package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.MaterialDtoRequest;
import com.ghopital.projet.dto.response.MaterialCreateDtoResponse;
import com.ghopital.projet.dto.response.MaterialDtoResponse;
import com.ghopital.projet.dto.response.QRCodeDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Material;
import com.ghopital.projet.entity.QRCode;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.MaterialRepository;
import com.ghopital.projet.repository.QRCodeRepository;
import com.ghopital.projet.service.QRCodeService;

import java.io.UnsupportedEncodingException;
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

@ContextConfiguration(classes = {MaterialServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class MaterialServiceImplTest {
    @MockBean
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialServiceImpl materialServiceImpl;

    @MockBean
    private QRCodeRepository qRCodeRepository;

    @MockBean
    private QRCodeService qRCodeService;

    /**
     * Method under test:
     * {@link MaterialServiceImpl#createMaterial(MaterialDtoRequest)}
     */
    @Test
    void testCreateMaterial() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        when(materialRepository.save(Mockito.<Material>any())).thenReturn(material);
        when(qRCodeService.generateQRCodeMaterial(Mockito.<Long>any()))
                .thenReturn(new QRCodeDtoResponse(1L, "AXAXAXAX".getBytes("UTF-8")));

        QRCode qrCode2 = new QRCode();
        qrCode2.setId(1L);
        qrCode2.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));
        Optional<QRCode> ofResult = Optional.of(qrCode2);
        when(qRCodeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        MaterialCreateDtoResponse actualCreateMaterialResult = materialServiceImpl
                .createMaterial(new MaterialDtoRequest("Name", "The characteristics of someone or something"));
        verify(qRCodeService).generateQRCodeMaterial(Mockito.<Long>any());
        verify(qRCodeRepository).findById(Mockito.<Long>any());
        verify(materialRepository, atLeast(1)).save(Mockito.<Material>any());
        assertEquals("Name", actualCreateMaterialResult.name());
        assertEquals("The characteristics of someone or something", actualCreateMaterialResult.description());
        assertEquals(1L, actualCreateMaterialResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link MaterialServiceImpl#createMaterial(MaterialDtoRequest)}
     */
    @Test
    void testCreateMaterial2() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        when(materialRepository.save(Mockito.<Material>any())).thenReturn(material);
        when(qRCodeService.generateQRCodeMaterial(Mockito.<Long>any()))
                .thenReturn(new QRCodeDtoResponse(1L, "AXAXAXAX".getBytes("UTF-8")));
        when(qRCodeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("QRCode", "QRCode", "42"));
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl
                .createMaterial(new MaterialDtoRequest("Name", "The characteristics of someone or something")));
        verify(qRCodeService).generateQRCodeMaterial(Mockito.<Long>any());
        verify(qRCodeRepository).findById(Mockito.<Long>any());
        verify(materialRepository).save(Mockito.<Material>any());
    }

    /**
     * Method under test: {@link MaterialServiceImpl#getById(Long)}
     */
    @Test
    void testGetById() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        MaterialDtoResponse actualById = materialServiceImpl.getById(1L);
        verify(materialRepository).findById(Mockito.<Long>any());
        assertEquals("Name", actualById.name());
        MaterialDtoResponse.StockDto stockResult = actualById.stock();
        MaterialDtoResponse.StockDto.LocationDto locationResult = stockResult.location();
        assertEquals("Name", locationResult.name());
        assertEquals("The characteristics of someone or something", actualById.description());
        assertEquals(1L, actualById.id().longValue());
        assertEquals(1L, stockResult.id().longValue());
        assertEquals(1L, stockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
    }

    /**
     * Method under test: {@link MaterialServiceImpl#getById(Long)}
     */
    @Test
    void testGetById2() {
        Optional<Material> emptyResult = Optional.empty();
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl.getById(1L));
        verify(materialRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link MaterialServiceImpl#getById(Long)}
     */
    @Test
    void testGetById3() {
        when(materialRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Material", "Material", "42"));
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl.getById(1L));
        verify(materialRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link MaterialServiceImpl#getAllMaterial()}
     */
    @Test
    void testGetAllMaterial() {
        when(materialRepository.findAll()).thenReturn(new ArrayList<>());
        List<MaterialDtoResponse> actualAllMaterial = materialServiceImpl.getAllMaterial();
        verify(materialRepository).findAll();
        assertTrue(actualAllMaterial.isEmpty());
    }

    /**
     * Method under test:  {@link MaterialServiceImpl#getAllMaterial()}
     */
    @Test
    void testGetAllMaterial2() {
        when(materialRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl.getAllMaterial());
        verify(materialRepository).findAll();
    }

    /**
     * Method under test:
     * {@link MaterialServiceImpl#updateMaterial(Long, MaterialDtoRequest)}
     */
    @Test
    void testUpdateMaterial() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);

        QRCode qrCode2 = new QRCode();
        qrCode2.setId(1L);
        qrCode2.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material2 = new Material();
        material2.setDeliveryNotes(new HashSet<>());
        material2.setDescription("The characteristics of someone or something");
        material2.setId(1L);
        material2.setName("Name");
        material2.setQrCode(qrCode2);
        material2.setStock(stock2);
        when(materialRepository.save(Mockito.<Material>any())).thenReturn(material2);
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        MaterialCreateDtoResponse actualUpdateMaterialResult = materialServiceImpl.updateMaterial(1L,
                new MaterialDtoRequest("Name", "The characteristics of someone or something"));
        verify(materialRepository).findById(Mockito.<Long>any());
        verify(materialRepository).save(Mockito.<Material>any());
        assertEquals("Name", actualUpdateMaterialResult.name());
        assertEquals("The characteristics of someone or something", actualUpdateMaterialResult.description());
        assertEquals(1L, actualUpdateMaterialResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link MaterialServiceImpl#updateMaterial(Long, MaterialDtoRequest)}
     */
    @Test
    void testUpdateMaterial2() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);
        when(materialRepository.save(Mockito.<Material>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl.updateMaterial(1L,
                new MaterialDtoRequest("Name", "The characteristics of someone or something")));
        verify(materialRepository).findById(Mockito.<Long>any());
        verify(materialRepository).save(Mockito.<Material>any());
    }

    /**
     * Method under test: {@link MaterialServiceImpl#deleteMaterial(Long)}
     */
    @Test
    void testDeleteMaterial() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);
        doNothing().when(materialRepository).delete(Mockito.<Material>any());
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        materialServiceImpl.deleteMaterial(1L);
        verify(materialRepository).delete(Mockito.<Material>any());
        verify(materialRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MaterialServiceImpl#deleteMaterial(Long)}
     */
    @Test
    void testDeleteMaterial2() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

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

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(materialRepository)
                .delete(Mockito.<Material>any());
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl.deleteMaterial(1L));
        verify(materialRepository).delete(Mockito.<Material>any());
        verify(materialRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MaterialServiceImpl#deleteMaterial(Long)}
     */
    @Test
    void testDeleteMaterial3() {
        Optional<Material> emptyResult = Optional.empty();
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> materialServiceImpl.deleteMaterial(1L));
        verify(materialRepository).findById(Mockito.<Long>any());
    }
}
