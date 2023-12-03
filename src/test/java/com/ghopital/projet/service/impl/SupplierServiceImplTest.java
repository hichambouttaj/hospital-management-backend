package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.SupplierDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.dto.response.SupplierDtoResponse;
import com.ghopital.projet.entity.Supplier;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.SupplierRepository;

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

@ContextConfiguration(classes = {SupplierServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class SupplierServiceImplTest {
    @MockBean
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierServiceImpl supplierServiceImpl;

    /**
     * Method under test:
     * {@link SupplierServiceImpl#createSupplier(SupplierDtoRequest)}
     */
    @Test
    void testCreateSupplier() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        when(supplierRepository.save(Mockito.<Supplier>any())).thenReturn(supplier);
        SupplierDtoResponse actualCreateSupplierResult = supplierServiceImpl
                .createSupplier(new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org"));
        verify(supplierRepository).save(Mockito.<Supplier>any());
        assertEquals("42 Main St", actualCreateSupplierResult.address());
        assertEquals("6625550144", actualCreateSupplierResult.phone());
        assertEquals("Name", actualCreateSupplierResult.name());
        assertEquals("jane.doe@example.org", actualCreateSupplierResult.email());
        assertEquals(1L, actualCreateSupplierResult.id().longValue());
    }

    /**
     * Method under test:  {@link SupplierServiceImpl#createSupplier(SupplierDtoRequest)}
     */
    @Test
    void testCreateSupplier2() {
        when(supplierRepository.save(Mockito.<Supplier>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl
                .createSupplier(new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org")));
        verify(supplierRepository).save(Mockito.<Supplier>any());
    }

    /**
     * Method under test: {@link SupplierServiceImpl#getById(Long)}
     */
    @Test
    void testGetById() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SupplierDtoResponse actualById = supplierServiceImpl.getById(1L);
        verify(supplierRepository).findById(Mockito.<Long>any());
        assertEquals("42 Main St", actualById.address());
        assertEquals("6625550144", actualById.phone());
        assertEquals("Name", actualById.name());
        assertEquals("jane.doe@example.org", actualById.email());
        assertEquals(1L, actualById.id().longValue());
    }

    /**
     * Method under test: {@link SupplierServiceImpl#getById(Long)}
     */
    @Test
    void testGetById2() {
        Optional<Supplier> emptyResult = Optional.empty();
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.getById(1L));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link SupplierServiceImpl#getById(Long)}
     */
    @Test
    void testGetById3() {
        when(supplierRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Supplier", "Supplier", "42"));
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.getById(1L));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link SupplierServiceImpl#getAllSupplier()}
     */
    @Test
    void testGetAllSupplier() {
        when(supplierRepository.findAll()).thenReturn(new ArrayList<>());
        List<SupplierDtoResponse> actualAllSupplier = supplierServiceImpl.getAllSupplier();
        verify(supplierRepository).findAll();
        assertTrue(actualAllSupplier.isEmpty());
    }

    /**
     * Method under test:  {@link SupplierServiceImpl#getAllSupplier()}
     */
    @Test
    void testGetAllSupplier2() {
        when(supplierRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.getAllSupplier());
        verify(supplierRepository).findAll();
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#getStockOrdersForSupplier(Long)}
     */
    @Test
    void testGetStockOrdersForSupplier() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        List<StockOrderDtoResponse> actualStockOrdersForSupplier = supplierServiceImpl.getStockOrdersForSupplier(1L);
        verify(supplierRepository).findById(Mockito.<Long>any());
        assertTrue(actualStockOrdersForSupplier.isEmpty());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#getStockOrdersForSupplier(Long)}
     */
    @Test
    void testGetStockOrdersForSupplier2() {
        Optional<Supplier> emptyResult = Optional.empty();
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.getStockOrdersForSupplier(1L));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link SupplierServiceImpl#getStockOrdersForSupplier(Long)}
     */
    @Test
    void testGetStockOrdersForSupplier3() {
        when(supplierRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Supplier", "Supplier", "42"));
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.getStockOrdersForSupplier(1L));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        when(supplierRepository.save(Mockito.<Supplier>any())).thenReturn(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SupplierDtoResponse actualUpdateSupplierResult = supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org"));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).save(Mockito.<Supplier>any());
        assertEquals("42 Main St", actualUpdateSupplierResult.address());
        assertEquals("6625550144", actualUpdateSupplierResult.phone());
        assertEquals("Name", actualUpdateSupplierResult.name());
        assertEquals("jane.doe@example.org", actualUpdateSupplierResult.email());
        assertEquals(1L, actualUpdateSupplierResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier2() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        when(supplierRepository.save(Mockito.<Supplier>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org")));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).save(Mockito.<Supplier>any());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier3() {
        Optional<Supplier> emptyResult = Optional.empty();
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org")));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier4() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        when(supplierRepository.save(Mockito.<Supplier>any())).thenReturn(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SupplierDtoResponse actualUpdateSupplierResult = supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("", "42 Main St", "6625550144", "jane.doe@example.org"));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).save(Mockito.<Supplier>any());
        assertEquals("42 Main St", actualUpdateSupplierResult.address());
        assertEquals("6625550144", actualUpdateSupplierResult.phone());
        assertEquals("Name", actualUpdateSupplierResult.name());
        assertEquals("jane.doe@example.org", actualUpdateSupplierResult.email());
        assertEquals(1L, actualUpdateSupplierResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier5() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        when(supplierRepository.save(Mockito.<Supplier>any())).thenReturn(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SupplierDtoResponse actualUpdateSupplierResult = supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("Name", "", "6625550144", "jane.doe@example.org"));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).save(Mockito.<Supplier>any());
        assertEquals("42 Main St", actualUpdateSupplierResult.address());
        assertEquals("6625550144", actualUpdateSupplierResult.phone());
        assertEquals("Name", actualUpdateSupplierResult.name());
        assertEquals("jane.doe@example.org", actualUpdateSupplierResult.email());
        assertEquals(1L, actualUpdateSupplierResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier6() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        when(supplierRepository.save(Mockito.<Supplier>any())).thenReturn(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SupplierDtoResponse actualUpdateSupplierResult = supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("Name", "42 Main St", "", "jane.doe@example.org"));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).save(Mockito.<Supplier>any());
        assertEquals("42 Main St", actualUpdateSupplierResult.address());
        assertEquals("6625550144", actualUpdateSupplierResult.phone());
        assertEquals("Name", actualUpdateSupplierResult.name());
        assertEquals("jane.doe@example.org", actualUpdateSupplierResult.email());
        assertEquals(1L, actualUpdateSupplierResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link SupplierServiceImpl#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier7() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        when(supplierRepository.save(Mockito.<Supplier>any())).thenReturn(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        SupplierDtoResponse actualUpdateSupplierResult = supplierServiceImpl.updateSupplier(1L,
                new SupplierDtoRequest("Name", "42 Main St", "6625550144", ""));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).save(Mockito.<Supplier>any());
        assertEquals("42 Main St", actualUpdateSupplierResult.address());
        assertEquals("6625550144", actualUpdateSupplierResult.phone());
        assertEquals("Name", actualUpdateSupplierResult.name());
        assertEquals("jane.doe@example.org", actualUpdateSupplierResult.email());
        assertEquals(1L, actualUpdateSupplierResult.id().longValue());
    }

    /**
     * Method under test: {@link SupplierServiceImpl#deleteSupplier(Long)}
     */
    @Test
    void testDeleteSupplier() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        doNothing().when(supplierRepository).delete(Mockito.<Supplier>any());
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        supplierServiceImpl.deleteSupplier(1L);
        verify(supplierRepository).delete(Mockito.<Supplier>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SupplierServiceImpl#deleteSupplier(Long)}
     */
    @Test
    void testDeleteSupplier2() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(supplierRepository)
                .delete(Mockito.<Supplier>any());
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.deleteSupplier(1L));
        verify(supplierRepository).delete(Mockito.<Supplier>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SupplierServiceImpl#deleteSupplier(Long)}
     */
    @Test
    void testDeleteSupplier3() {
        Optional<Supplier> emptyResult = Optional.empty();
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> supplierServiceImpl.deleteSupplier(1L));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }
}
