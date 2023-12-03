package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.ServiceDtoResponse;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.ServiceRepository;

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

@ContextConfiguration(classes = {IHospitalServicesImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class IHospitalServicesImplTest {
    @Autowired
    private IHospitalServicesImpl iHospitalServicesImpl;

    @MockBean
    private ServiceRepository serviceRepository;

    /**
     * Method under test:
     * {@link IHospitalServicesImpl#createService(ServiceDtoRequest)}
     */
    @Test
    void testCreateService() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        when(serviceRepository.save(Mockito.<HospitalService>any())).thenReturn(hospitalService);
        ServiceDtoResponse actualCreateServiceResult = iHospitalServicesImpl.createService(new ServiceDtoRequest("Name"));
        verify(serviceRepository).save(Mockito.<HospitalService>any());
        assertEquals("Name", actualCreateServiceResult.name());
        assertEquals(1L, actualCreateServiceResult.id().longValue());
    }

    /**
     * Method under test:  {@link IHospitalServicesImpl#createService(ServiceDtoRequest)}
     */
    @Test
    void testCreateService2() {
        when(serviceRepository.save(Mockito.<HospitalService>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class,
                () -> iHospitalServicesImpl.createService(new ServiceDtoRequest("Name")));
        verify(serviceRepository).save(Mockito.<HospitalService>any());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#getServiceById(Long)}
     */
    @Test
    void testGetServiceById() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ServiceDtoResponse actualServiceById = iHospitalServicesImpl.getServiceById(1L);
        verify(serviceRepository).findById(Mockito.<Long>any());
        assertEquals("Name", actualServiceById.name());
        assertEquals(1L, actualServiceById.id().longValue());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#getServiceById(Long)}
     */
    @Test
    void testGetServiceById2() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.getServiceById(1L));
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link IHospitalServicesImpl#getServiceById(Long)}
     */
    @Test
    void testGetServiceById3() {
        when(serviceRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Service", "Service", "42"));
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.getServiceById(1L));
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#getServiceByName(String)}
     */
    @Test
    void testGetServiceByName() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        ServiceDtoResponse actualServiceByName = iHospitalServicesImpl.getServiceByName("Service Name");
        verify(serviceRepository).findByName(Mockito.<String>any());
        assertEquals("Name", actualServiceByName.name());
        assertEquals(1L, actualServiceByName.id().longValue());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#getServiceByName(String)}
     */
    @Test
    void testGetServiceByName2() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.getServiceByName("Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link IHospitalServicesImpl#getServiceByName(String)}
     */
    @Test
    void testGetServiceByName3() {
        when(serviceRepository.findByName(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("Service", "Service", "42"));
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.getServiceByName("Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link IHospitalServicesImpl#getAllServices()}
     */
    @Test
    void testGetAllServices() {
        when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
        List<ServiceDtoResponse> actualAllServices = iHospitalServicesImpl.getAllServices();
        verify(serviceRepository).findAll();
        assertTrue(actualAllServices.isEmpty());
    }

    /**
     * Method under test:  {@link IHospitalServicesImpl#getAllServices()}
     */
    @Test
    void testGetAllServices2() {
        when(serviceRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.getAllServices());
        verify(serviceRepository).findAll();
    }

    /**
     * Method under test:
     * {@link IHospitalServicesImpl#updateService(ServiceDtoRequest, Long)}
     */
    @Test
    void testUpdateService() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        when(serviceRepository.save(Mockito.<HospitalService>any())).thenReturn(hospitalService2);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ServiceDtoResponse actualUpdateServiceResult = iHospitalServicesImpl.updateService(new ServiceDtoRequest("Name"),
                1L);
        verify(serviceRepository).findById(Mockito.<Long>any());
        verify(serviceRepository).save(Mockito.<HospitalService>any());
        assertEquals("Name", actualUpdateServiceResult.name());
        assertEquals(1L, actualUpdateServiceResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link IHospitalServicesImpl#updateService(ServiceDtoRequest, Long)}
     */
    @Test
    void testUpdateService2() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.save(Mockito.<HospitalService>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class,
                () -> iHospitalServicesImpl.updateService(new ServiceDtoRequest("Name"), 1L));
        verify(serviceRepository).findById(Mockito.<Long>any());
        verify(serviceRepository).save(Mockito.<HospitalService>any());
    }

    /**
     * Method under test:
     * {@link IHospitalServicesImpl#updateService(ServiceDtoRequest, Long)}
     */
    @Test
    void testUpdateService3() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> iHospitalServicesImpl.updateService(new ServiceDtoRequest("Name"), 1L));
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link IHospitalServicesImpl#updateService(ServiceDtoRequest, Long)}
     */
    @Test
    void testUpdateService4() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        when(serviceRepository.save(Mockito.<HospitalService>any())).thenReturn(hospitalService2);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ServiceDtoResponse actualUpdateServiceResult = iHospitalServicesImpl.updateService(new ServiceDtoRequest(""), 1L);
        verify(serviceRepository).findById(Mockito.<Long>any());
        verify(serviceRepository).save(Mockito.<HospitalService>any());
        assertEquals("Name", actualUpdateServiceResult.name());
        assertEquals(1L, actualUpdateServiceResult.id().longValue());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#deleteService(Long)}
     */
    @Test
    void testDeleteService() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        doNothing().when(serviceRepository).delete(Mockito.<HospitalService>any());
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        iHospitalServicesImpl.deleteService(1L);
        verify(serviceRepository).delete(Mockito.<HospitalService>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#deleteService(Long)}
     */
    @Test
    void testDeleteService2() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(serviceRepository)
                .delete(Mockito.<HospitalService>any());
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.deleteService(1L));
        verify(serviceRepository).delete(Mockito.<HospitalService>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IHospitalServicesImpl#deleteService(Long)}
     */
    @Test
    void testDeleteService3() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> iHospitalServicesImpl.deleteService(1L));
        verify(serviceRepository).findById(Mockito.<Long>any());
    }
}
