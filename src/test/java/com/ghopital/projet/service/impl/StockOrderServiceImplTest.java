package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.StockOrderDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.entity.DeliveryNote;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Order;
import com.ghopital.projet.entity.Product;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.entity.StockOrder;
import com.ghopital.projet.entity.Supplier;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.DeliveryNoteRepository;
import com.ghopital.projet.repository.OrderRepository;
import com.ghopital.projet.repository.ProductRepository;
import com.ghopital.projet.repository.StockOrderRepository;
import com.ghopital.projet.repository.StockRepository;
import com.ghopital.projet.repository.SupplierRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

@ContextConfiguration(classes = {StockOrderServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class StockOrderServiceImplTest {
    @MockBean
    private DeliveryNoteRepository deliveryNoteRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private StockOrderRepository stockOrderRepository;

    @Autowired
    private StockOrderServiceImpl stockOrderServiceImpl;

    @MockBean
    private StockRepository stockRepository;

    @MockBean
    private SupplierRepository supplierRepository;

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#createStockOrder(StockOrderDtoRequest)}
     */
    @Test
    void testCreateStockOrder() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        StockOrderDtoResponse actualCreateStockOrderResult = stockOrderServiceImpl
                .createStockOrder(new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location"));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository, atLeast(1)).save(Mockito.<StockOrder>any());
        assertEquals("00:00", actualCreateStockOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualCreateStockOrderResult.code());
        assertEquals("Delivery Location", actualCreateStockOrderResult.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = actualCreateStockOrderResult.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1L, supplierResult.id().longValue());
        assertTrue(actualCreateStockOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:  {@link StockOrderServiceImpl#createStockOrder(StockOrderDtoRequest)}
     */
    @Test
    void testCreateStockOrder2() {
        when(stockOrderRepository.save(Mockito.<StockOrder>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));

        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl
                .createStockOrder(new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location")));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository).save(Mockito.<StockOrder>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#createStockOrder(StockOrderDtoRequest)}
     */
    @Test
    void testCreateStockOrder3() {
        Optional<Supplier> emptyResult = Optional.empty();
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl
                .createStockOrder(new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location")));
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#createStockOrder(StockOrderDtoRequest)}
     */
    @Test
    void testCreateStockOrder4() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Order order = new Order();
        order.setCode(1L);
        order.setDeliveryNotes(new HashSet<>());
        order.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setId(1L);
        deliveryNote.setOrder(order);
        deliveryNote.setProduct(product);
        deliveryNote.setQuantity(1L);
        when(deliveryNoteRepository.save(Mockito.<DeliveryNote>any())).thenReturn(deliveryNote);

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

        Location location3 = new Location();
        location3.setAddress("42 Main St");
        location3.setDescription("The characteristics of someone or something");
        location3.setId(1L);
        location3.setLatitude("Latitude");
        location3.setLongitude("Longitude");
        location3.setName("Name");
        location3.setStocks(new HashSet<>());

        Stock stock3 = new Stock();
        stock3.setId(1L);
        stock3.setLocation(location3);
        stock3.setQuantity(1L);

        Product product2 = new Product();
        product2.setDeliveryNotes(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setName("Name");
        product2.setStock(stock3);
        Optional<Product> ofResult2 = Optional.of(product2);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        HashSet<StockOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new StockOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        StockOrderDtoResponse actualCreateStockOrderResult = stockOrderServiceImpl.createStockOrder(
                new StockOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, 1L, "Delivery Location"));
        verify(productRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(deliveryNoteRepository).save(Mockito.<DeliveryNote>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        verify(stockOrderRepository, atLeast(1)).save(Mockito.<StockOrder>any());
        assertEquals("00:00", actualCreateStockOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualCreateStockOrderResult.code());
        assertEquals("Delivery Location", actualCreateStockOrderResult.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = actualCreateStockOrderResult.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1L, supplierResult.id().longValue());
        assertTrue(actualCreateStockOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#createStockOrder(StockOrderDtoRequest)}
     */
    @Test
    void testCreateStockOrder5() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Order order = new Order();
        order.setCode(1L);
        order.setDeliveryNotes(new HashSet<>());
        order.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setId(1L);
        deliveryNote.setOrder(order);
        deliveryNote.setProduct(product);
        deliveryNote.setQuantity(1L);
        when(deliveryNoteRepository.save(Mockito.<DeliveryNote>any())).thenReturn(deliveryNote);
        when(stockRepository.save(Mockito.<Stock>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));

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

        Product product2 = new Product();
        product2.setDeliveryNotes(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setName("Name");
        product2.setStock(stock2);
        Optional<Product> ofResult2 = Optional.of(product2);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        HashSet<StockOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new StockOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.createStockOrder(
                new StockOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, 1L, "Delivery Location")));
        verify(productRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        verify(stockOrderRepository).save(Mockito.<StockOrder>any());
    }

    /**
     * Method under test: {@link StockOrderServiceImpl#getStockOrder(Long)}
     */
    @Test
    void testGetStockOrder() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        StockOrderDtoResponse actualStockOrder = stockOrderServiceImpl.getStockOrder(1L);
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        assertEquals("00:00", actualStockOrder.orderDate().toLocalTime().toString());
        assertEquals("1", actualStockOrder.code());
        assertEquals("Delivery Location", actualStockOrder.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = actualStockOrder.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1L, supplierResult.id().longValue());
        assertTrue(actualStockOrder.deliveryNotes().isEmpty());
    }

    /**
     * Method under test: {@link StockOrderServiceImpl#getStockOrder(Long)}
     */
    @Test
    void testGetStockOrder2() {
        Optional<StockOrder> emptyResult = Optional.empty();
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.getStockOrder(1L));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link StockOrderServiceImpl#getStockOrder(Long)}
     */
    @Test
    void testGetStockOrder3() {
        when(stockOrderRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Stock Order", "Stock Order", "42"));
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.getStockOrder(1L));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link StockOrderServiceImpl#getAllStockOrder()}
     */
    @Test
    void testGetAllStockOrder() {
        when(stockOrderRepository.findAll()).thenReturn(new ArrayList<>());
        List<StockOrderDtoResponse> actualAllStockOrder = stockOrderServiceImpl.getAllStockOrder();
        verify(stockOrderRepository).findAll();
        assertTrue(actualAllStockOrder.isEmpty());
    }

    /**
     * Method under test:  {@link StockOrderServiceImpl#getAllStockOrder()}
     */
    @Test
    void testGetAllStockOrder2() {
        when(stockOrderRepository.findAll()).thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.getAllStockOrder());
        verify(stockOrderRepository).findAll();
    }

    /**
     * Method under test:  {@link StockOrderServiceImpl#getStockOrdersOfProduct(Long)}
     */
    @Test
    void testGetStockOrdersOfProduct() {
        when(stockOrderRepository.findAllByProduct(Mockito.<Long>any())).thenReturn(new ArrayList<>());

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
        List<StockOrderDtoResponse> actualStockOrdersOfProduct = stockOrderServiceImpl.getStockOrdersOfProduct(1L);
        verify(stockOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        assertTrue(actualStockOrdersOfProduct.isEmpty());
    }

    /**
     * Method under test:  {@link StockOrderServiceImpl#getStockOrdersOfProduct(Long)}
     */
    @Test
    void testGetStockOrdersOfProduct2() {
        when(stockOrderRepository.findAllByProduct(Mockito.<Long>any()))
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
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.getStockOrdersOfProduct(1L));
        verify(stockOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#getStockOrdersOfProduct(Long)}
     */
    @Test
    void testGetStockOrdersOfProduct3() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);

        ArrayList<StockOrder> stockOrderList = new ArrayList<>();
        stockOrderList.add(stockOrder);
        when(stockOrderRepository.findAllByProduct(Mockito.<Long>any())).thenReturn(stockOrderList);

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
        List<StockOrderDtoResponse> actualStockOrdersOfProduct = stockOrderServiceImpl.getStockOrdersOfProduct(1L);
        verify(stockOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        StockOrderDtoResponse getResult = actualStockOrdersOfProduct.get(0);
        assertEquals("00:00", getResult.orderDate().toLocalTime().toString());
        assertEquals("1", getResult.code());
        assertEquals("Delivery Location", getResult.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = getResult.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1, actualStockOrdersOfProduct.size());
        assertEquals(1L, supplierResult.id().longValue());
        assertTrue(getResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#getStockOrdersOfProduct(Long)}
     */
    @Test
    void testGetStockOrdersOfProduct4() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("17 High St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("john.smith@example.org");
        supplier2.setId(2L);
        supplier2.setName("42");
        supplier2.setPhone("8605550118");

        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setCode(16L);
        stockOrder2.setDeliveryLocation("42");
        stockOrder2.setDeliveryNotes(new HashSet<>());
        stockOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder2.setSupplier(supplier2);

        ArrayList<StockOrder> stockOrderList = new ArrayList<>();
        stockOrderList.add(stockOrder2);
        stockOrderList.add(stockOrder);
        when(stockOrderRepository.findAllByProduct(Mockito.<Long>any())).thenReturn(stockOrderList);

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
        List<StockOrderDtoResponse> actualStockOrdersOfProduct = stockOrderServiceImpl.getStockOrdersOfProduct(1L);
        verify(stockOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        StockOrderDtoResponse getResult = actualStockOrdersOfProduct.get(0);
        assertEquals("00:00", getResult.orderDate().toLocalTime().toString());
        StockOrderDtoResponse getResult2 = actualStockOrdersOfProduct.get(1);
        assertEquals("1", getResult2.code());
        assertEquals("16", getResult.code());
        assertEquals("1970-01-01", getResult2.orderDate().toLocalDate().toString());
        assertEquals("42", getResult.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = getResult.supplier();
        assertEquals("42", supplierResult.name());
        assertEquals("Delivery Location", getResult2.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult2 = getResult2.supplier();
        assertEquals("Name", supplierResult2.name());
        assertEquals(1L, supplierResult2.id().longValue());
        assertEquals(2, actualStockOrdersOfProduct.size());
        assertEquals(2L, supplierResult.id().longValue());
        assertTrue(getResult.deliveryNotes().isEmpty());
        assertTrue(getResult2.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#getStockOrdersOfProduct(Long)}
     */
    @Test
    void testGetStockOrdersOfProduct5() {
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.getStockOrdersOfProduct(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");

        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setCode(1L);
        stockOrder2.setDeliveryLocation("Delivery Location");
        stockOrder2.setDeliveryNotes(new HashSet<>());
        stockOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder2.setSupplier(supplier2);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder2);
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Supplier supplier3 = new Supplier();
        supplier3.setAddress("42 Main St");
        supplier3.setDeliveryNotes(new HashSet<>());
        supplier3.setEmail("jane.doe@example.org");
        supplier3.setId(1L);
        supplier3.setName("Name");
        supplier3.setPhone("6625550144");
        Optional<Supplier> ofResult2 = Optional.of(supplier3);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        StockOrderDtoResponse actualUpdateStockOrderResult = stockOrderServiceImpl.updateStockOrder(1L,
                new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location"));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository).save(Mockito.<StockOrder>any());
        assertEquals("00:00", actualUpdateStockOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualUpdateStockOrderResult.code());
        assertEquals("Delivery Location", actualUpdateStockOrderResult.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = actualUpdateStockOrderResult.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1L, supplierResult.id().longValue());
        assertTrue(actualUpdateStockOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder2() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);
        when(stockOrderRepository.save(Mockito.<StockOrder>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        Optional<Supplier> ofResult2 = Optional.of(supplier2);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.updateStockOrder(1L,
                new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location")));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository).save(Mockito.<StockOrder>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder3() {
        Optional<StockOrder> emptyResult = Optional.empty();
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.updateStockOrder(1L,
                new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location")));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder4() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Supplier> emptyResult = Optional.empty();
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.updateStockOrder(1L,
                new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location")));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder5() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");

        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setCode(1L);
        stockOrder2.setDeliveryLocation("Delivery Location");
        stockOrder2.setDeliveryNotes(new HashSet<>());
        stockOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder2.setSupplier(supplier2);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder2);
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Supplier supplier3 = new Supplier();
        supplier3.setAddress("42 Main St");
        supplier3.setDeliveryNotes(new HashSet<>());
        supplier3.setEmail("jane.doe@example.org");
        supplier3.setId(1L);
        supplier3.setName("Name");
        supplier3.setPhone("6625550144");
        Optional<Supplier> ofResult2 = Optional.of(supplier3);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        Order order = new Order();
        order.setCode(1L);
        order.setDeliveryNotes(new HashSet<>());
        order.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setId(1L);
        deliveryNote.setOrder(order);
        deliveryNote.setProduct(product);
        deliveryNote.setQuantity(1L);
        when(deliveryNoteRepository.save(Mockito.<DeliveryNote>any())).thenReturn(deliveryNote);

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

        Location location3 = new Location();
        location3.setAddress("42 Main St");
        location3.setDescription("The characteristics of someone or something");
        location3.setId(1L);
        location3.setLatitude("Latitude");
        location3.setLongitude("Longitude");
        location3.setName("Name");
        location3.setStocks(new HashSet<>());

        Stock stock3 = new Stock();
        stock3.setId(1L);
        stock3.setLocation(location3);
        stock3.setQuantity(1L);

        Product product2 = new Product();
        product2.setDeliveryNotes(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setName("Name");
        product2.setStock(stock3);
        Optional<Product> ofResult3 = Optional.of(product2);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);

        HashSet<StockOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new StockOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        StockOrderDtoResponse actualUpdateStockOrderResult = stockOrderServiceImpl.updateStockOrder(1L,
                new StockOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, 1L, "Delivery Location"));
        verify(productRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(deliveryNoteRepository).save(Mockito.<DeliveryNote>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        verify(stockOrderRepository).save(Mockito.<StockOrder>any());
        assertEquals("00:00", actualUpdateStockOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualUpdateStockOrderResult.code());
        assertEquals("Delivery Location", actualUpdateStockOrderResult.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = actualUpdateStockOrderResult.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1L, supplierResult.id().longValue());
        assertTrue(actualUpdateStockOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link StockOrderServiceImpl#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder6() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");

        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setCode(1L);
        stockOrder2.setDeliveryLocation("Delivery Location");
        stockOrder2.setDeliveryNotes(new HashSet<>());
        stockOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder2.setSupplier(supplier2);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder2);
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Supplier supplier3 = new Supplier();
        supplier3.setAddress("42 Main St");
        supplier3.setDeliveryNotes(new HashSet<>());
        supplier3.setEmail("jane.doe@example.org");
        supplier3.setId(1L);
        supplier3.setName("Name");
        supplier3.setPhone("6625550144");
        Optional<Supplier> ofResult2 = Optional.of(supplier3);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        Order order = new Order();
        order.setCode(1L);
        order.setDeliveryNotes(new HashSet<>());
        order.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setId(1L);
        deliveryNote.setOrder(order);
        deliveryNote.setProduct(product);
        deliveryNote.setQuantity(1L);
        when(deliveryNoteRepository.save(Mockito.<DeliveryNote>any())).thenReturn(deliveryNote);
        when(stockRepository.save(Mockito.<Stock>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));

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

        Product product2 = new Product();
        product2.setDeliveryNotes(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setName("Name");
        product2.setStock(stock2);
        Optional<Product> ofResult3 = Optional.of(product2);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);

        HashSet<StockOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new StockOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.updateStockOrder(1L,
                new StockOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, 1L, "Delivery Location")));
        verify(productRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link StockOrderServiceImpl#deleteStockOrder(Long)}
     */
    @Test
    void testDeleteStockOrder() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);
        doNothing().when(stockOrderRepository).delete(Mockito.<StockOrder>any());
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        stockOrderServiceImpl.deleteStockOrder(1L);
        verify(stockOrderRepository).delete(Mockito.<StockOrder>any());
        verify(stockOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockOrderServiceImpl#deleteStockOrder(Long)}
     */
    @Test
    void testDeleteStockOrder2() {
        Supplier supplier = new Supplier();
        supplier.setAddress("42 Main St");
        supplier.setDeliveryNotes(new HashSet<>());
        supplier.setEmail("jane.doe@example.org");
        supplier.setId(1L);
        supplier.setName("Name");
        supplier.setPhone("6625550144");

        StockOrder stockOrder = new StockOrder();
        stockOrder.setCode(1L);
        stockOrder.setDeliveryLocation("Delivery Location");
        stockOrder.setDeliveryNotes(new HashSet<>());
        stockOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        stockOrder.setSupplier(supplier);
        Optional<StockOrder> ofResult = Optional.of(stockOrder);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(stockOrderRepository)
                .delete(Mockito.<StockOrder>any());
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.deleteStockOrder(1L));
        verify(stockOrderRepository).delete(Mockito.<StockOrder>any());
        verify(stockOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link StockOrderServiceImpl#deleteStockOrder(Long)}
     */
    @Test
    void testDeleteStockOrder3() {
        Optional<StockOrder> emptyResult = Optional.empty();
        when(stockOrderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> stockOrderServiceImpl.deleteStockOrder(1L));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
    }
}
