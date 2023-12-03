package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.ProductOrderDtoRequest;
import com.ghopital.projet.dto.response.ProductOrderDtoResponse;
import com.ghopital.projet.entity.DeliveryNote;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Order;
import com.ghopital.projet.entity.Product;
import com.ghopital.projet.entity.ProductOrder;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.DeliveryNoteRepository;
import com.ghopital.projet.repository.OrderRepository;
import com.ghopital.projet.repository.ProductOrderRepository;
import com.ghopital.projet.repository.ProductRepository;
import com.ghopital.projet.repository.ServiceRepository;
import com.ghopital.projet.repository.StockRepository;

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

@ContextConfiguration(classes = {ProductOrderServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class ProductOrderServiceImplTest {
    @MockBean
    private DeliveryNoteRepository deliveryNoteRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductOrderServiceImpl productOrderServiceImpl;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ServiceRepository serviceRepository;

    @MockBean
    private StockRepository stockRepository;

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#createProductOrder(ProductOrderDtoRequest)}
     */
    @Test
    void testCreateProductOrder() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ProductOrderDtoResponse actualCreateProductOrderResult = productOrderServiceImpl
                .createProductOrder(new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository, atLeast(1)).save(Mockito.<ProductOrder>any());
        assertEquals("00:00", actualCreateProductOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualCreateProductOrderResult.code());
        assertEquals("Name", actualCreateProductOrderResult.hospitalServiceName());
        assertTrue(actualCreateProductOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:  {@link ProductOrderServiceImpl#createProductOrder(ProductOrderDtoRequest)}
     */
    @Test
    void testCreateProductOrder2() {
        when(productOrderRepository.save(Mockito.<ProductOrder>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl
                .createProductOrder(new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).save(Mockito.<ProductOrder>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#createProductOrder(ProductOrderDtoRequest)}
     */
    @Test
    void testCreateProductOrder3() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl
                .createProductOrder(new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#createProductOrder(ProductOrderDtoRequest)}
     */
    @Test
    void testCreateProductOrder4() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);

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

        HashSet<ProductOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new ProductOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        ProductOrderDtoResponse actualCreateProductOrderResult = productOrderServiceImpl.createProductOrder(
                new ProductOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, "Hospital Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productRepository).findById(Mockito.<Long>any());
        verify(deliveryNoteRepository).save(Mockito.<DeliveryNote>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        verify(productOrderRepository, atLeast(1)).save(Mockito.<ProductOrder>any());
        assertEquals("00:00", actualCreateProductOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualCreateProductOrderResult.code());
        assertEquals("Name", actualCreateProductOrderResult.hospitalServiceName());
        assertTrue(actualCreateProductOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#createProductOrder(ProductOrderDtoRequest)}
     */
    @Test
    void testCreateProductOrder5() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);

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

        HashSet<ProductOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new ProductOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.createProductOrder(
                new ProductOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, "Hospital Service Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productRepository).findById(Mockito.<Long>any());
        verify(productOrderRepository).save(Mockito.<ProductOrder>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link ProductOrderServiceImpl#getProductOrder(Long)}
     */
    @Test
    void testGetProductOrder() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ProductOrderDtoResponse actualProductOrder = productOrderServiceImpl.getProductOrder(1L);
        verify(productOrderRepository).findById(Mockito.<Long>any());
        assertEquals("00:00", actualProductOrder.orderDate().toLocalTime().toString());
        assertEquals("1", actualProductOrder.code());
        assertEquals("Name", actualProductOrder.hospitalServiceName());
        assertTrue(actualProductOrder.deliveryNotes().isEmpty());
    }

    /**
     * Method under test: {@link ProductOrderServiceImpl#getProductOrder(Long)}
     */
    @Test
    void testGetProductOrder2() {
        Optional<ProductOrder> emptyResult = Optional.empty();
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.getProductOrder(1L));
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link ProductOrderServiceImpl#getProductOrder(Long)}
     */
    @Test
    void testGetProductOrder3() {
        when(productOrderRepository.findById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("Product Order", "Product Order", "42"));
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.getProductOrder(1L));
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link ProductOrderServiceImpl#getAllProductOrder()}
     */
    @Test
    void testGetAllProductOrder() {
        when(productOrderRepository.findAll()).thenReturn(new ArrayList<>());
        List<ProductOrderDtoResponse> actualAllProductOrder = productOrderServiceImpl.getAllProductOrder();
        verify(productOrderRepository).findAll();
        assertTrue(actualAllProductOrder.isEmpty());
    }

    /**
     * Method under test:  {@link ProductOrderServiceImpl#getAllProductOrder()}
     */
    @Test
    void testGetAllProductOrder2() {
        when(productOrderRepository.findAll())
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.getAllProductOrder());
        verify(productOrderRepository).findAll();
    }

    /**
     * Method under test:  {@link ProductOrderServiceImpl#getProductOrdersOfProduct(Long)}
     */
    @Test
    void testGetProductOrdersOfProduct() {
        when(productOrderRepository.findAllByProduct(Mockito.<Long>any())).thenReturn(new ArrayList<>());

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
        List<ProductOrderDtoResponse> actualProductOrdersOfProduct = productOrderServiceImpl.getProductOrdersOfProduct(1L);
        verify(productOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        assertTrue(actualProductOrdersOfProduct.isEmpty());
    }

    /**
     * Method under test:  {@link ProductOrderServiceImpl#getProductOrdersOfProduct(Long)}
     */
    @Test
    void testGetProductOrdersOfProduct2() {
        when(productOrderRepository.findAllByProduct(Mockito.<Long>any()))
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
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.getProductOrdersOfProduct(1L));
        verify(productOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#getProductOrdersOfProduct(Long)}
     */
    @Test
    void testGetProductOrdersOfProduct3() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<ProductOrder> productOrderList = new ArrayList<>();
        productOrderList.add(productOrder);
        when(productOrderRepository.findAllByProduct(Mockito.<Long>any())).thenReturn(productOrderList);

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
        List<ProductOrderDtoResponse> actualProductOrdersOfProduct = productOrderServiceImpl.getProductOrdersOfProduct(1L);
        verify(productOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        ProductOrderDtoResponse getResult = actualProductOrdersOfProduct.get(0);
        assertEquals("00:00", getResult.orderDate().toLocalTime().toString());
        assertEquals("1", getResult.code());
        assertEquals("Name", getResult.hospitalServiceName());
        assertEquals(1, actualProductOrdersOfProduct.size());
        assertTrue(getResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#getProductOrdersOfProduct(Long)}
     */
    @Test
    void testGetProductOrdersOfProduct4() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(2L);
        hospitalService2.setName("42");
        hospitalService2.setProductOrders(new HashSet<>());

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setCode(16L);
        productOrder2.setDeliveryNotes(new HashSet<>());
        productOrder2.setHospitalService(hospitalService2);
        productOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<ProductOrder> productOrderList = new ArrayList<>();
        productOrderList.add(productOrder2);
        productOrderList.add(productOrder);
        when(productOrderRepository.findAllByProduct(Mockito.<Long>any())).thenReturn(productOrderList);

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
        List<ProductOrderDtoResponse> actualProductOrdersOfProduct = productOrderServiceImpl.getProductOrdersOfProduct(1L);
        verify(productOrderRepository).findAllByProduct(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        ProductOrderDtoResponse getResult = actualProductOrdersOfProduct.get(0);
        assertEquals("00:00", getResult.orderDate().toLocalTime().toString());
        ProductOrderDtoResponse getResult2 = actualProductOrdersOfProduct.get(1);
        assertEquals("00:00", getResult2.orderDate().toLocalTime().toString());
        assertEquals("1", getResult2.code());
        assertEquals("16", getResult.code());
        assertEquals("42", getResult.hospitalServiceName());
        assertEquals("Name", getResult2.hospitalServiceName());
        assertEquals(2, actualProductOrdersOfProduct.size());
        assertTrue(getResult.deliveryNotes().isEmpty());
        assertTrue(getResult2.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#getProductOrdersOfProduct(Long)}
     */
    @Test
    void testGetProductOrdersOfProduct5() {
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.getProductOrdersOfProduct(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setCode(1L);
        productOrder2.setDeliveryNotes(new HashSet<>());
        productOrder2.setHospitalService(hospitalService2);
        productOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder2);
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ProductOrderDtoResponse actualUpdateProductOrderResult = productOrderServiceImpl.updateProductOrder(1L,
                new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
        verify(productOrderRepository).save(Mockito.<ProductOrder>any());
        assertEquals("00:00", actualUpdateProductOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualUpdateProductOrderResult.code());
        assertEquals("Name", actualUpdateProductOrderResult.hospitalServiceName());
        assertTrue(actualUpdateProductOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder2() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);
        when(productOrderRepository.save(Mockito.<ProductOrder>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42"));
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.updateProductOrder(1L,
                new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
        verify(productOrderRepository).save(Mockito.<ProductOrder>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder3() {
        Optional<ProductOrder> emptyResult = Optional.empty();
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.updateProductOrder(1L,
                new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name")));
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder4() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.updateProductOrder(1L,
                new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder5() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setCode(1L);
        productOrder2.setDeliveryNotes(new HashSet<>());
        productOrder2.setHospitalService(hospitalService2);
        productOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder2);
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);

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

        HashSet<ProductOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new ProductOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        ProductOrderDtoResponse actualUpdateProductOrderResult = productOrderServiceImpl.updateProductOrder(1L,
                new ProductOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, "Hospital Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        verify(deliveryNoteRepository).save(Mockito.<DeliveryNote>any());
        verify(productOrderRepository).save(Mockito.<ProductOrder>any());
        verify(stockRepository).save(Mockito.<Stock>any());
        assertEquals("00:00", actualUpdateProductOrderResult.orderDate().toLocalTime().toString());
        assertEquals("1", actualUpdateProductOrderResult.code());
        assertEquals("Name", actualUpdateProductOrderResult.hospitalServiceName());
        assertTrue(actualUpdateProductOrderResult.deliveryNotes().isEmpty());
    }

    /**
     * Method under test:
     * {@link ProductOrderServiceImpl#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder6() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setCode(1L);
        productOrder2.setDeliveryNotes(new HashSet<>());
        productOrder2.setHospitalService(hospitalService2);
        productOrder2.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder2);
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);

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

        HashSet<ProductOrderDtoRequest.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        deliveryNotes.add(new ProductOrderDtoRequest.DeliveryNoteDto(1L, 1L));
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.updateProductOrder(1L,
                new ProductOrderDtoRequest(LocalDate.of(1970, 1, 1).atStartOfDay(), deliveryNotes, "Hospital Service Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
        verify(productRepository).findById(Mockito.<Long>any());
        verify(stockRepository).save(Mockito.<Stock>any());
    }

    /**
     * Method under test: {@link ProductOrderServiceImpl#deleteProductOrder(Long)}
     */
    @Test
    void testDeleteProductOrder() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);
        doNothing().when(productOrderRepository).delete(Mockito.<ProductOrder>any());
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        productOrderServiceImpl.deleteProductOrder(1L);
        verify(productOrderRepository).delete(Mockito.<ProductOrder>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ProductOrderServiceImpl#deleteProductOrder(Long)}
     */
    @Test
    void testDeleteProductOrder2() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<ProductOrder> ofResult = Optional.of(productOrder);
        doThrow(new ResourceNotFoundException("Resource Name", "Field Name", "42")).when(productOrderRepository)
                .delete(Mockito.<ProductOrder>any());
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.deleteProductOrder(1L));
        verify(productOrderRepository).delete(Mockito.<ProductOrder>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ProductOrderServiceImpl#deleteProductOrder(Long)}
     */
    @Test
    void testDeleteProductOrder3() {
        Optional<ProductOrder> emptyResult = Optional.empty();
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> productOrderServiceImpl.deleteProductOrder(1L));
        verify(productOrderRepository).findById(Mockito.<Long>any());
    }
}
