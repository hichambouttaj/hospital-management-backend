package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.MedicationDtoRequest;
import com.ghopital.projet.dto.response.MedicationDtoResponse;
import com.ghopital.projet.service.MedicationService;

import java.math.BigDecimal;
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

@ContextConfiguration(classes = {MedicationController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class MedicationControllerTest {
    @Autowired
    private MedicationController medicationController;

    @MockBean
    private MedicationService medicationService;

    /**
     * Method under test:  {@link MedicationController#getAllMedication()}
     */
    @Test
    void testGetAllMedication() throws Exception {
        when(medicationService.getAllMedication()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/medications");
        MockMvcBuilders.standaloneSetup(medicationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link MedicationController#updateMedication(Long, MedicationDtoRequest)}
     */
    @Test
    void testUpdateMedication() throws Exception {
        MedicationDtoResponse.StockDto stock = new MedicationDtoResponse.StockDto(1L, 1L,
                new MedicationDtoResponse.StockDto.LocationDto(1L, "Name"));

        when(medicationService.updateMedication(Mockito.<Long>any(), Mockito.<MedicationDtoRequest>any()))
                .thenReturn(new MedicationDtoResponse(1L, "Name", "The characteristics of someone or something", stock,
                        "Manufacturer", new BigDecimal("2.3")));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .put("/api/v1/medications/{medicationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new MedicationDtoRequest("Name",
                        "The characteristics of someone or something", "Manufacturer", new BigDecimal("2.3"))));
        MockMvcBuilders.standaloneSetup(medicationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"stock\":{\"id\":1,"
                                        + "\"quantity\":1,\"location\":{\"id\":1,\"name\":\"Name\"}},\"manufacturer\":\"Manufacturer\",\"price\":2.3}"));
    }

    /**
     * Method under test:  {@link MedicationController#createMedication(MedicationDtoRequest)}
     */
    @Test
    void testCreateMedication() throws Exception {
        when(medicationService.getAllMedication()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/medications")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new MedicationDtoRequest("Name",
                        "The characteristics of someone or something", "Manufacturer", new BigDecimal("2.3"))));
        MockMvcBuilders.standaloneSetup(medicationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link MedicationController#deleteMedication(Long)}
     */
    @Test
    void testDeleteMedication() throws Exception {
        doNothing().when(medicationService).deleteMedication(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/medications/{medicationId}",
                1L);
        MockMvcBuilders.standaloneSetup(medicationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Medication deleted successfully"));
    }

    /**
     * Method under test: {@link MedicationController#deleteMedication(Long)}
     */
    @Test
    void testDeleteMedication2() throws Exception {
        doNothing().when(medicationService).deleteMedication(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/medications/{medicationId}",
                1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(medicationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Medication deleted successfully"));
    }

    /**
     * Method under test: {@link MedicationController#getMedicationById(Long)}
     */
    @Test
    void testGetMedicationById() throws Exception {
        MedicationDtoResponse.StockDto stock = new MedicationDtoResponse.StockDto(1L, 1L,
                new MedicationDtoResponse.StockDto.LocationDto(1L, "Name"));

        when(medicationService.getById(Mockito.<Long>any())).thenReturn(new MedicationDtoResponse(1L, "Name",
                "The characteristics of someone or something", stock, "Manufacturer", new BigDecimal("2.3")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/medications/{medicationId}", 1L);
        MockMvcBuilders.standaloneSetup(medicationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"stock\":{\"id\":1,"
                                        + "\"quantity\":1,\"location\":{\"id\":1,\"name\":\"Name\"}},\"manufacturer\":\"Manufacturer\",\"price\":2.3}"));
    }
}
