package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.LocationDtoRequest;
import com.ghopital.projet.dto.response.LocationDtoResponse;
import com.ghopital.projet.service.LocationService;

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

@ContextConfiguration(classes = {LocationController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class LocationControllerTest {
    @Autowired
    private LocationController locationController;

    @MockBean
    private LocationService locationService;

    /**
     * Method under test:  {@link LocationController#getAllLocation()}
     */
    @Test
    void testGetAllLocation() throws Exception {
        when(locationService.getAllLocation()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/locations");
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link LocationController#updateLocation(Long, LocationDtoRequest)}
     */
    @Test
    void testUpdateLocation() throws Exception {
        when(locationService.updateLocation(Mockito.<Long>any(), Mockito.<LocationDtoRequest>any()))
                .thenReturn(new LocationDtoResponse(1L, "Name", "The characteristics of someone or something", "42 Main St",
                        "Latitude", "Longitude"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/locations/{locationId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LocationDtoRequest("Name",
                        "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude")));
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"address\":\"42 Main"
                                        + " St\",\"latitude\":\"Latitude\",\"longitude\":\"Longitude\"}"));
    }

    /**
     * Method under test:  {@link LocationController#createLocation(LocationDtoRequest)}
     */
    @Test
    void testCreateLocation() throws Exception {
        when(locationService.getAllLocation()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LocationDtoRequest("Name",
                        "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude")));
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link LocationController#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation() throws Exception {
        doNothing().when(locationService).deleteLocation(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/locations/{locationId}", 1L);
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Location deleted successfully"));
    }

    /**
     * Method under test: {@link LocationController#deleteLocation(Long)}
     */
    @Test
    void testDeleteLocation2() throws Exception {
        doNothing().when(locationService).deleteLocation(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/locations/{locationId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Location deleted successfully"));
    }

    /**
     * Method under test:  {@link LocationController#getLocationById(Long)}
     */
    @Test
    void testGetLocationById() throws Exception {
        when(locationService.getById(Mockito.<Long>any())).thenReturn(new LocationDtoResponse(1L, "Name",
                "The characteristics of someone or something", "42 Main St", "Latitude", "Longitude"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/locations/{locationId}", 1L);
        MockMvcBuilders.standaloneSetup(locationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"address\":\"42 Main"
                                        + " St\",\"latitude\":\"Latitude\",\"longitude\":\"Longitude\"}"));
    }
}
