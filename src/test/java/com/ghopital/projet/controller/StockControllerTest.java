package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.StockDtoRequest;
import com.ghopital.projet.dto.request.StockUpdateDtoRequest;
import com.ghopital.projet.dto.response.StockDtoResponse;
import com.ghopital.projet.service.StockService;

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

@ContextConfiguration(classes = {StockController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class StockControllerTest {
    @Autowired
    private StockController stockController;

    @MockBean
    private StockService stockService;

    /**
     * Method under test:  {@link StockController#getAllStock()}
     */
    @Test
    void testGetAllStock() throws Exception {
        when(stockService.getAllStock()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/stocks");
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link StockController#updateStock(Long, StockUpdateDtoRequest)}
     */
    @Test
    void testUpdateStock() throws Exception {
        StockDtoResponse.ProductDto product = new StockDtoResponse.ProductDto(1L, "Name");

        when(stockService.updateStock(Mockito.<Long>any(), Mockito.<StockUpdateDtoRequest>any()))
                .thenReturn(new StockDtoResponse(1L, 1L, product, new StockDtoResponse.LocationDto(1L, "Name")));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/stocks/{stockId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new StockUpdateDtoRequest(1L)));
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"quantity\":1,\"product\":{\"id\":1,\"name\":\"Name\"},\"location\":{\"id\":1,\"name\":\"Name\"}}"));
    }

    /**
     * Method under test: {@link StockController#addLocationToStock(Long, Long)}
     */
    @Test
    void testAddLocationToStock() throws Exception {
        StockDtoResponse.ProductDto product = new StockDtoResponse.ProductDto(1L, "Name");

        when(stockService.addLocationToStock(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new StockDtoResponse(1L, 1L, product, new StockDtoResponse.LocationDto(1L, "Name")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/stocks/{stockId}/locations/{locationId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"quantity\":1,\"product\":{\"id\":1,\"name\":\"Name\"},\"location\":{\"id\":1,\"name\":\"Name\"}}"));
    }

    /**
     * Method under test: {@link StockController#deleteStock(Long)}
     */
    @Test
    void testDeleteStock() throws Exception {
        doNothing().when(stockService).deleteStock(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/stocks/{stockId}", 1L);
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Stock deleted successfully"));
    }

    /**
     * Method under test: {@link StockController#deleteStock(Long)}
     */
    @Test
    void testDeleteStock2() throws Exception {
        doNothing().when(stockService).deleteStock(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/stocks/{stockId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Stock deleted successfully"));
    }

    /**
     * Method under test:  {@link StockController#createStock(StockDtoRequest)}
     */
    @Test
    void testCreateStock() throws Exception {
        when(stockService.getAllStock()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/stocks")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new StockDtoRequest(1L, 1L)));
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link StockController#getStockById(Long)}
     */
    @Test
    void testGetStockById() throws Exception {
        StockDtoResponse.ProductDto product = new StockDtoResponse.ProductDto(1L, "Name");

        when(stockService.getById(Mockito.<Long>any()))
                .thenReturn(new StockDtoResponse(1L, 1L, product, new StockDtoResponse.LocationDto(1L, "Name")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/stocks/{stockId}", 1L);
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"quantity\":1,\"product\":{\"id\":1,\"name\":\"Name\"},\"location\":{\"id\":1,\"name\":\"Name\"}}"));
    }

    /**
     * Method under test: {@link StockController#getStockOfProduct(Long)}
     */
    @Test
    void testGetStockOfProduct() throws Exception {
        StockDtoResponse.ProductDto product = new StockDtoResponse.ProductDto(1L, "Name");

        when(stockService.getStockByProduct(Mockito.<Long>any()))
                .thenReturn(new StockDtoResponse(1L, 1L, product, new StockDtoResponse.LocationDto(1L, "Name")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/stocks/products/{productId}",
                1L);
        MockMvcBuilders.standaloneSetup(stockController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"quantity\":1,\"product\":{\"id\":1,\"name\":\"Name\"},\"location\":{\"id\":1,\"name\":\"Name\"}}"));
    }
}
