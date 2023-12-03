package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.MaterialDtoRequest;
import com.ghopital.projet.dto.response.MaterialCreateDtoResponse;
import com.ghopital.projet.dto.response.MaterialDtoResponse;
import com.ghopital.projet.service.MaterialService;

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

@ContextConfiguration(classes = {MaterialController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class MaterialControllerTest {
    @Autowired
    private MaterialController materialController;

    @MockBean
    private MaterialService materialService;

    /**
     * Method under test:  {@link MaterialController#getAllMaterial()}
     */
    @Test
    void testGetAllMaterial() throws Exception {
        when(materialService.getAllMaterial()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/materials");
        MockMvcBuilders.standaloneSetup(materialController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link MaterialController#updateMaterial(Long, MaterialDtoRequest)}
     */
    @Test
    void testUpdateMaterial() throws Exception {
        when(materialService.updateMaterial(Mockito.<Long>any(), Mockito.<MaterialDtoRequest>any()))
                .thenReturn(new MaterialCreateDtoResponse(1L, "Name", "The characteristics of someone or something"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/materials/{materialId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(
                objectMapper.writeValueAsString(new MaterialDtoRequest("Name", "The characteristics of someone or something")));
        MockMvcBuilders.standaloneSetup(materialController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\"}"));
    }

    /**
     * Method under test:  {@link MaterialController#createMaterial(MaterialDtoRequest)}
     */
    @Test
    void testCreateMaterial() throws Exception {
        when(materialService.getAllMaterial()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/materials")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(
                objectMapper.writeValueAsString(new MaterialDtoRequest("Name", "The characteristics of someone or something")));
        MockMvcBuilders.standaloneSetup(materialController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link MaterialController#deleteMaterial(Long)}
     */
    @Test
    void testDeleteMaterial() throws Exception {
        doNothing().when(materialService).deleteMaterial(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/materials/{materialId}", 1L);
        MockMvcBuilders.standaloneSetup(materialController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Material deleted successfully"));
    }

    /**
     * Method under test: {@link MaterialController#deleteMaterial(Long)}
     */
    @Test
    void testDeleteMaterial2() throws Exception {
        doNothing().when(materialService).deleteMaterial(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/materials/{materialId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(materialController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Material deleted successfully"));
    }

    /**
     * Method under test:  {@link MaterialController#getMaterialById(Long)}
     */
    @Test
    void testGetMaterialById() throws Exception {
        when(materialService.getById(Mockito.<Long>any()))
                .thenReturn(new MaterialDtoResponse(1L, "Name", "The characteristics of someone or something",
                        new MaterialDtoResponse.StockDto(1L, 1L, new MaterialDtoResponse.StockDto.LocationDto(1L, "Name"))));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/materials/{materialId}", 1L);
        MockMvcBuilders.standaloneSetup(materialController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"stock\":{\"id\":1,"
                                        + "\"quantity\":1,\"location\":{\"id\":1,\"name\":\"Name\"}}}"));
    }
}
