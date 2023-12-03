package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.StockDtoRequest;
import com.ghopital.projet.dto.request.StockUpdateDtoRequest;
import com.ghopital.projet.dto.response.StockDtoResponse;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Product;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.LocationRepository;
import com.ghopital.projet.repository.ProductRepository;
import com.ghopital.projet.repository.StockRepository;

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

@ContextConfiguration(classes = {StockServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class StockServiceImplTest {
    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private StockRepository stockRepository;

    @Autowired
    private StockServiceImpl stockServiceImpl;

    /**
     * Method under test: {@link StockServiceImpl#createStock(StockDtoRequest)}
     */
    @Test
    void testCreateStock() {
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
        when(stockRepository.save(Mockito.<Stock>any())).thenReturn(stock);

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

        Product product = new Product();
        product.setDeliveryNotes(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setName("Name");
        product.setStock(stock2);
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        StockDtoResponse actualCreateStockResult = stockServiceImpl.createStock(new StockDtoRequest(1L, 1L));
        verify(productRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        StockDtoResponse.LocationDto locationResult = actualCreateStockResult.location();
        assertEquals("Name", locationResult.name());
        assertNull(actualCreateStockResult.product());
        assertEquals(1L, actualCreateStockResult.id().longValue());
        assertEquals(1L, actualCreateStockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
    }

    /**
     * Method under test:  {@link StockServiceImpl#createStock(StockDtoRequest)}
     */
    @Test
    void testCreateStock2() {
        when(stockRepository.save(Mockito.<Stock>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));

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

        Product product = new Product();
        product.setDeliveryNotes(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setName("Name");
        product.setStock(stock);
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.createStock(new StockDtoRequest(1L, 1L)));
        verify(productRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#createStock(StockDtoRequest)}
     */
    @Test
    void testCreateStock3() {
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.createStock(new StockDtoRequest(1L, 1L)));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#getById(Long)}
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
        Optional<Stock> ofResult = Optional.of(stock);
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        StockDtoResponse actualById = stockServiceImpl.getById(1L);
        verify(stockRepository).findById(Mockito.<Long>any());
        StockDtoResponse.LocationDto locationResult = actualById.location();
        assertEquals("Name", locationResult.name());
        assertNull(actualById.product());
        assertEquals(1L, actualById.id().longValue());
        assertEquals(1L, actualById.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
    }

    /**
     * Method under test: {@link StockServiceImpl#getById(Long)}
     */
    @Test
    void testGetById2() {
        Optional<Stock> emptyResult = Optional.empty();
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.getById(1L));
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link StockServiceImpl#getById(Long)}
     */
    @Test
    void testGetById3() {
        when(stockRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Stock", "Stock", "42"));
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.getById(1L));
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#getStockByProduct(Long)}
     */
    @Test
    void testGetStockByProduct() {
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

        Product product = new Product();
        product.setDeliveryNotes(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setName("Name");
        product.setStock(stock);
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        StockDtoResponse actualStockByProduct = stockServiceImpl.getStockByProduct(1L);
        verify(productRepository).findById(Mockito.<Long>any());
        StockDtoResponse.LocationDto locationResult = actualStockByProduct.location();
        assertEquals("Name", locationResult.name());
        assertNull(actualStockByProduct.product());
        assertEquals(1L, actualStockByProduct.id().longValue());
        assertEquals(1L, actualStockByProduct.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
    }

    /**
     * Method under test: {@link StockServiceImpl#getStockByProduct(Long)}
     */
    @Test
    void testGetStockByProduct2() {
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.getStockByProduct(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link StockServiceImpl#getStockByProduct(Long)}
     */
    @Test
    void testGetStockByProduct3() {
        when(productRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Product", "Product", "42"));
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.getStockByProduct(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link StockServiceImpl#getAllStock()}
     */
    @Test
    void testGetAllStock() {
        when(stockRepository.findAll()).thenReturn(new ArrayList<>());
        List<StockDtoResponse> actualAllStock = stockServiceImpl.getAllStock();
        verify(stockRepository).findAll();
        assertTrue(actualAllStock.isEmpty());
    }

    /**
     * Method under test:  {@link StockServiceImpl#getAllStock()}
     */
    @Test
    void testGetAllStock2() {
        when(stockRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.getAllStock());
        verify(stockRepository).findAll();
    }

    /**
     * Method under test:
     * {@link StockServiceImpl#updateStock(Long, StockUpdateDtoRequest)}
     */
    @Test
    void testUpdateStock() {
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
        Optional<Stock> ofResult = Optional.of(stock);

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
        when(stockRepository.save(Mockito.<Stock>any())).thenReturn(stock2);
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        StockDtoResponse actualUpdateStockResult = stockServiceImpl.updateStock(1L, new StockUpdateDtoRequest(1L));
        verify(stockRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        StockDtoResponse.LocationDto locationResult = actualUpdateStockResult.location();
        assertEquals("Name", locationResult.name());
        assertNull(actualUpdateStockResult.product());
        assertEquals(1L, actualUpdateStockResult.id().longValue());
        assertEquals(1L, actualUpdateStockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link StockServiceImpl#updateStock(Long, StockUpdateDtoRequest)}
     */
    @Test
    void testUpdateStock2() {
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
        Optional<Stock> ofResult = Optional.of(stock);
        when(stockRepository.save(Mockito.<Stock>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class,
                () -> stockServiceImpl.updateStock(1L, new StockUpdateDtoRequest(1L)));
        verify(stockRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test:
     * {@link StockServiceImpl#updateStock(Long, StockUpdateDtoRequest)}
     */
    @Test
    void testUpdateStock3() {
        Optional<Stock> emptyResult = Optional.empty();
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> stockServiceImpl.updateStock(1L, new StockUpdateDtoRequest(1L)));
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#addLocationToStock(Long, Long)}
     */
    @Test
    void testAddLocationToStock() {
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
        Optional<Stock> ofResult = Optional.of(stock);

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
        when(stockRepository.save(Mockito.<Stock>any())).thenReturn(stock2);
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Location location3 = new Location();
        location3.setAddress("42 Main St");
        location3.setDescription("The characteristics of someone or something");
        location3.setId(1L);
        location3.setLatitude("Latitude");
        location3.setLongitude("Longitude");
        location3.setName("Name");
        location3.setStocks(new HashSet<>());
        Optional<Location> ofResult2 = Optional.of(location3);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        StockDtoResponse actualAddLocationToStockResult = stockServiceImpl.addLocationToStock(1L, 1L);
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(stockRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        StockDtoResponse.LocationDto locationResult = actualAddLocationToStockResult.location();
        assertEquals("Name", locationResult.name());
        assertNull(actualAddLocationToStockResult.product());
        assertEquals(1L, actualAddLocationToStockResult.id().longValue());
        assertEquals(1L, actualAddLocationToStockResult.quantity().longValue());
        assertEquals(1L, locationResult.id().longValue());
    }

    /**
     * Method under test: {@link StockServiceImpl#addLocationToStock(Long, Long)}
     */
    @Test
    void testAddLocationToStock2() {
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
        Optional<Stock> ofResult = Optional.of(stock);
        when(stockRepository.save(Mockito.<Stock>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());
        Optional<Location> ofResult2 = Optional.of(location2);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.addLocationToStock(1L, 1L));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(stockRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#addLocationToStock(Long, Long)}
     */
    @Test
    void testAddLocationToStock3() {
        Optional<Stock> emptyResult = Optional.empty();
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());
        Optional<Location> ofResult = Optional.of(location);
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.addLocationToStock(1L, 1L));
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#addLocationToStock(Long, Long)}
     */
    @Test
    void testAddLocationToStock4() {
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
        Optional<Stock> ofResult = Optional.of(stock);
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Location> emptyResult = Optional.empty();
        when(locationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.addLocationToStock(1L, 1L));
        verify(locationRepository).findById(Mockito.<Long>any());
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#deleteStock(Long)}
     */
    @Test
    void testDeleteStock() {
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
        Optional<Stock> ofResult = Optional.of(stock);
        doNothing().when(stockRepository).delete(Mockito.<Stock>any());
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        stockServiceImpl.deleteStock(1L);
        verify(stockRepository).delete(Mockito.<Stock>any());
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#deleteStock(Long)}
     */
    @Test
    void testDeleteStock2() {
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
        Optional<Stock> ofResult = Optional.of(stock);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(stockRepository)
                .delete(Mockito.<Stock>any());
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.deleteStock(1L));
        verify(stockRepository).delete(Mockito.<Stock>any());
        verify(stockRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockServiceImpl#deleteStock(Long)}
     */
    @Test
    void testDeleteStock3() {
        Optional<Stock> emptyResult = Optional.empty();
        when(stockRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockServiceImpl.deleteStock(1L));
        verify(stockRepository).findById(Mockito.<Long>any());
    }
}
