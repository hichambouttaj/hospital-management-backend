package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.ServiceDtoResponse;
import com.ghopital.projet.service.IHospitalServices;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {HospitalServiceController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class HospitalServiceControllerTest {
    @Autowired
    private HospitalServiceController hospitalServiceController;

    @MockBean
    private IHospitalServices iHospitalServices;

    /**
     * Method under test:  {@link HospitalServiceController#getServiceById(Long)}
     */
    @Test
    void testGetServiceById() throws Exception {
        when(iHospitalServices.getServiceById(Mockito.<Long>any())).thenReturn(new ServiceDtoResponse(1L, "Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/services/{serviceId}", 1L);
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test:  {@link HospitalServiceController#getServiceByName(String)}
     */
    @Test
    void testGetServiceByName() throws Exception {
        when(iHospitalServices.getServiceByName(Mockito.<String>any())).thenReturn(new ServiceDtoResponse(1L, "Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/services")
                .param("serviceName", "foo");
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test:  {@link HospitalServiceController#updateService(Long, ServiceDtoRequest)}
     */
    @Test
    void testUpdateService() throws Exception {
        when(iHospitalServices.updateService(Mockito.<ServiceDtoRequest>any(), Mockito.<Long>any()))
                .thenReturn(new ServiceDtoResponse(1L, "Name"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/services/{serviceId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ServiceDtoRequest("Name")));
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test:  {@link HospitalServiceController#createService(ServiceDtoRequest)}
     */
    @Test
    void testCreateService() throws Exception {
        when(iHospitalServices.getAllServices()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ServiceDtoRequest("Name")));
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link HospitalServiceController#deleteService(Long)}
     */
    @Test
    void testDeleteService() throws Exception {
        doNothing().when(iHospitalServices).deleteService(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/services/{serviceId}", 1L);
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Service deleted successfully"));
    }

    /**
     * Method under test: {@link HospitalServiceController#deleteService(Long)}
     */
    @Test
    void testDeleteService2() throws Exception {
        doNothing().when(iHospitalServices).deleteService(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/services/{serviceId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Service deleted successfully"));
    }

    /**
     * Method under test:  {@link HospitalServiceController#getAllService()}
     */
    @Test
    void testGetAllService() throws Exception {
        when(iHospitalServices.getAllServices()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/services");
        MockMvcBuilders.standaloneSetup(hospitalServiceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
