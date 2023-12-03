package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.StockOrderDtoRequest;
import com.ghopital.projet.dto.response.StockOrderDtoResponse;
import com.ghopital.projet.entity.StockOrder;
import com.ghopital.projet.entity.Supplier;
import com.ghopital.projet.repository.DeliveryNoteRepository;
import com.ghopital.projet.repository.OrderRepository;
import com.ghopital.projet.repository.ProductRepository;
import com.ghopital.projet.repository.StockOrderRepository;
import com.ghopital.projet.repository.StockRepository;
import com.ghopital.projet.repository.SupplierRepository;
import com.ghopital.projet.service.StockOrderService;
import com.ghopital.projet.service.impl.StockOrderServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {StockOrderController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class StockOrderControllerTest {
    @Autowired
    private StockOrderController stockOrderController;

    @MockBean
    private StockOrderService stockOrderService;

    /**
     * Method under test:
     * {@link StockOrderController#createStockOrder(StockOrderDtoRequest)}
     */
    @Test
    void testCreateStockOrder() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.StockOrderDtoRequest["orderDate"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1308)
        //       at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:772)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:479)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:318)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4719)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3964)
        //   See https://diff.blue/R013 to resolve this issue.

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
        StockOrderRepository stockOrderRepository = mock(StockOrderRepository.class);
        when(stockOrderRepository.save(Mockito.<StockOrder>any())).thenReturn(stockOrder);

        Supplier supplier2 = new Supplier();
        supplier2.setAddress("42 Main St");
        supplier2.setDeliveryNotes(new HashSet<>());
        supplier2.setEmail("jane.doe@example.org");
        supplier2.setId(1L);
        supplier2.setName("Name");
        supplier2.setPhone("6625550144");
        Optional<Supplier> ofResult = Optional.of(supplier2);
        SupplierRepository supplierRepository = mock(SupplierRepository.class);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        StockOrderController stockOrderController = new StockOrderController(
                new StockOrderServiceImpl(stockOrderRepository, supplierRepository, mock(DeliveryNoteRepository.class),
                        mock(StockRepository.class), mock(ProductRepository.class), mock(OrderRepository.class)));
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<StockOrderDtoResponse> actualCreateStockOrderResult = stockOrderController
                .createStockOrder(new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location"));
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository, atLeast(1)).save(Mockito.<StockOrder>any());
        StockOrderDtoResponse body = actualCreateStockOrderResult.getBody();
        assertEquals("1", body.code());
        assertEquals("1970-01-01", body.orderDate().toLocalDate().toString());
        assertEquals("Delivery Location", body.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = body.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1, actualCreateStockOrderResult.getHeaders().size());
        assertEquals(1L, supplierResult.id().longValue());
        assertEquals(201, actualCreateStockOrderResult.getStatusCodeValue());
        assertTrue(body.deliveryNotes().isEmpty());
        assertTrue(actualCreateStockOrderResult.hasBody());
    }

    /**
     * Method under test: {@link StockOrderController#getStockOrder(Long)}
     */
    @Test
    void testGetStockOrder() throws Exception {
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        HashSet<StockOrderDtoResponse.DeliveryNoteDto> deliveryNotes = new HashSet<>();
        when(stockOrderService.getStockOrder(Mockito.<Long>any())).thenReturn(new StockOrderDtoResponse("Code", orderDate,
                deliveryNotes, new StockOrderDtoResponse.SupplierDto(1L, "Name"), "Delivery Location"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/stockOrders/{code}", 1L);
        MockMvcBuilders.standaloneSetup(stockOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"code\":\"Code\",\"orderDate\":[1970,1,1,0,0],\"deliveryNotes\":[],\"supplier\":{\"id\":1,\"name\":\"Name\"},"
                                        + "\"deliveryLocation\":\"Delivery Location\"}"));
    }

    /**
     * Method under test:  {@link StockOrderController#getAllStockOrder()}
     */
    @Test
    void testGetAllStockOrder() throws Exception {
        when(stockOrderService.getAllStockOrder()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/stockOrders");
        MockMvcBuilders.standaloneSetup(stockOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link StockOrderController#getStockOrdersOfProduct(Long)}
     */
    @Test
    void testGetStockOrdersOfProduct() throws Exception {
        when(stockOrderService.getStockOrdersOfProduct(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/stockOrders/products/{productId}", 1L);
        MockMvcBuilders.standaloneSetup(stockOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link StockOrderController#updateStockOrder(Long, StockOrderDtoRequest)}
     */
    @Test
    void testUpdateStockOrder() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.StockOrderDtoRequest["orderDate"])
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1308)
        //       at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
        //       at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
        //       at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:772)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:479)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:318)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4719)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3964)
        //   See https://diff.blue/R013 to resolve this issue.

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
        StockOrderRepository stockOrderRepository = mock(StockOrderRepository.class);
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
        SupplierRepository supplierRepository = mock(SupplierRepository.class);
        when(supplierRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        StockOrderController stockOrderController = new StockOrderController(
                new StockOrderServiceImpl(stockOrderRepository, supplierRepository, mock(DeliveryNoteRepository.class),
                        mock(StockRepository.class), mock(ProductRepository.class), mock(OrderRepository.class)));
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<StockOrderDtoResponse> actualUpdateStockOrderResult = stockOrderController.updateStockOrder(1L,
                new StockOrderDtoRequest(orderDate, new HashSet<>(), 1L, "Delivery Location"));
        verify(stockOrderRepository).findById(Mockito.<Long>any());
        verify(supplierRepository).findById(Mockito.<Long>any());
        verify(stockOrderRepository).save(Mockito.<StockOrder>any());
        StockOrderDtoResponse body = actualUpdateStockOrderResult.getBody();
        assertEquals("1", body.code());
        assertEquals("1970-01-01", body.orderDate().toLocalDate().toString());
        assertEquals("Delivery Location", body.deliveryLocation());
        StockOrderDtoResponse.SupplierDto supplierResult = body.supplier();
        assertEquals("Name", supplierResult.name());
        assertEquals(1L, supplierResult.id().longValue());
        assertEquals(200, actualUpdateStockOrderResult.getStatusCodeValue());
        assertTrue(body.deliveryNotes().isEmpty());
        assertTrue(actualUpdateStockOrderResult.hasBody());
        assertTrue(actualUpdateStockOrderResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link StockOrderController#deleteStockOrder(Long)}
     */
    @Test
    void testDeleteStockOrder() throws Exception {
        doNothing().when(stockOrderService).deleteStockOrder(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/stockOrders/{code}", 1L);
        MockMvcBuilders.standaloneSetup(stockOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Stock Order deleted successfully"));
    }

    /**
     * Method under test: {@link StockOrderController#deleteStockOrder(Long)}
     */
    @Test
    void testDeleteStockOrder2() throws Exception {
        doNothing().when(stockOrderService).deleteStockOrder(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/stockOrders/{code}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(stockOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Stock Order deleted successfully"));
    }
}
