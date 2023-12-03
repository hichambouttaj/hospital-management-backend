package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.SupplierDtoRequest;
import com.ghopital.projet.dto.response.SupplierDtoResponse;
import com.ghopital.projet.service.SupplierService;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {SupplierController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class SupplierControllerTest {
    @Autowired
    private SupplierController supplierController;

    @MockBean
    private SupplierService supplierService;

    /**
     * Method under test:  {@link SupplierController#getAllSupplier()}
     */
    @Test
    void testGetAllSupplier() throws Exception {
        when(supplierService.getAllSupplier()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/suppliers");
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link SupplierController#getStockOrdersForSupplier(Long)}
     */
    @Test
    void testGetStockOrdersForSupplier() throws Exception {
        when(supplierService.getStockOrdersForSupplier(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/suppliers/{supplierId}/deliveryNotes", 1L);
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link SupplierController#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier2() throws Exception {
        when(supplierService.updateSupplier(Mockito.<Long>any(), Mockito.<SupplierDtoRequest>any()))
                .thenReturn(new SupplierDtoResponse(1L, "Name", "42 Main St", "6625550144", "jane.doe@example.org"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/suppliers/{supplierId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new SupplierDtoRequest("Name", "42 Main St", "+212999999999", "jane.doe@example.org")));
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"address\":\"42 Main St\",\"phone\":\"6625550144\",\"email\":\"jane.doe@example.org\"}"));
    }

    /**
     * Method under test:  {@link SupplierController#createSupplier(SupplierDtoRequest)}
     */
    @Test
    void testCreateSupplier() throws Exception {
        when(supplierService.getAllSupplier()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org")));
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link SupplierController#deleteSupplier(Long)}
     */
    @Test
    void testDeleteSupplier() throws Exception {
        doNothing().when(supplierService).deleteSupplier(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/suppliers/{supplierId}", 1L);
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Supplier deleted successfully"));
    }

    /**
     * Method under test: {@link SupplierController#deleteSupplier(Long)}
     */
    @Test
    void testDeleteSupplier2() throws Exception {
        doNothing().when(supplierService).deleteSupplier(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/suppliers/{supplierId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Supplier deleted successfully"));
    }

    /**
     * Method under test:  {@link SupplierController#getSupplierById(Long)}
     */
    @Test
    void testGetSupplierById() throws Exception {
        when(supplierService.getById(Mockito.<Long>any()))
                .thenReturn(new SupplierDtoResponse(1L, "Name", "42 Main St", "6625550144", "jane.doe@example.org"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/suppliers/{supplierId}", 1L);
        MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"name\":\"Name\",\"address\":\"42 Main St\",\"phone\":\"6625550144\",\"email\":\"jane.doe@example.org\"}"));
    }

    /**
     * Method under test:
     * {@link SupplierController#updateSupplier(Long, SupplierDtoRequest)}
     */
    @Test
    void testUpdateSupplier() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/suppliers/{supplierId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new SupplierDtoRequest("Name", "42 Main St", "6625550144", "jane.doe@example.org")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(supplierController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
