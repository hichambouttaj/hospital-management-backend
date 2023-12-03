package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.ProductOrderDtoRequest;
import com.ghopital.projet.dto.response.ProductOrderDtoResponse;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.entity.ProductOrder;
import com.ghopital.projet.repository.DeliveryNoteRepository;
import com.ghopital.projet.repository.OrderRepository;
import com.ghopital.projet.repository.ProductOrderRepository;
import com.ghopital.projet.repository.ProductRepository;
import com.ghopital.projet.repository.ServiceRepository;
import com.ghopital.projet.repository.StockRepository;
import com.ghopital.projet.service.ProductOrderService;
import com.ghopital.projet.service.impl.ProductOrderServiceImpl;

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

@ContextConfiguration(classes = {ProductOrderController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class ProductOrderControllerTest {
    @Autowired
    private ProductOrderController productOrderController;

    @MockBean
    private ProductOrderService productOrderService;

    /**
     * Method under test:
     * {@link ProductOrderController#createProductOrder(ProductOrderDtoRequest)}
     */
    @Test
    void testCreateProductOrder() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.ProductOrderDtoRequest["orderDate"])
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

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCode(1L);
        productOrder.setDeliveryNotes(new HashSet<>());
        productOrder.setHospitalService(hospitalService);
        productOrder.setOrderDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        ProductOrderRepository productOrderRepository = mock(ProductOrderRepository.class);
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService2);
        ServiceRepository serviceRepository = mock(ServiceRepository.class);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        ProductOrderController productOrderController = new ProductOrderController(
                new ProductOrderServiceImpl(productOrderRepository, serviceRepository, mock(DeliveryNoteRepository.class),
                        mock(StockRepository.class), mock(ProductRepository.class), mock(OrderRepository.class)));
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<ProductOrderDtoResponse> actualCreateProductOrderResult = productOrderController
                .createProductOrder(new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository, atLeast(1)).save(Mockito.<ProductOrder>any());
        ProductOrderDtoResponse body = actualCreateProductOrderResult.getBody();
        assertEquals("00:00", body.orderDate().toLocalTime().toString());
        assertEquals("1", body.code());
        assertEquals("Name", body.hospitalServiceName());
        assertEquals(1, actualCreateProductOrderResult.getHeaders().size());
        assertEquals(201, actualCreateProductOrderResult.getStatusCodeValue());
        assertTrue(body.deliveryNotes().isEmpty());
        assertTrue(actualCreateProductOrderResult.hasBody());
    }

    /**
     * Method under test: {@link ProductOrderController#getProductOrder(Long)}
     */
    @Test
    void testGetProductOrder() throws Exception {
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(productOrderService.getProductOrder(Mockito.<Long>any()))
                .thenReturn(new ProductOrderDtoResponse("Code", orderDate, new HashSet<>(), "Hospital Service Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/productOrders/{code}", 1L);
        MockMvcBuilders.standaloneSetup(productOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"code\":\"Code\",\"orderDate\":[1970,1,1,0,0],\"deliveryNotes\":[],\"hospitalServiceName\":\"Hospital Service"
                                        + " Name\"}"));
    }

    /**
     * Method under test:  {@link ProductOrderController#getAllProductOrder()}
     */
    @Test
    void testGetAllProductOrder() throws Exception {
        when(productOrderService.getAllProductOrder()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/productOrders");
        MockMvcBuilders.standaloneSetup(productOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link ProductOrderController#getProductOrdersOfProduct(Long)}
     */
    @Test
    void testGetProductOrdersOfProduct() throws Exception {
        when(productOrderService.getProductOrdersOfProduct(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/productOrders/products/{productId}", 1L);
        MockMvcBuilders.standaloneSetup(productOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link ProductOrderController#updateProductOrder(Long, ProductOrderDtoRequest)}
     */
    @Test
    void testUpdateProductOrder() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.ProductOrderDtoRequest["orderDate"])
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
        ProductOrderRepository productOrderRepository = mock(ProductOrderRepository.class);
        when(productOrderRepository.save(Mockito.<ProductOrder>any())).thenReturn(productOrder2);
        when(productOrderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        ServiceRepository serviceRepository = mock(ServiceRepository.class);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        ProductOrderController productOrderController = new ProductOrderController(
                new ProductOrderServiceImpl(productOrderRepository, serviceRepository, mock(DeliveryNoteRepository.class),
                        mock(StockRepository.class), mock(ProductRepository.class), mock(OrderRepository.class)));
        LocalDateTime orderDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<ProductOrderDtoResponse> actualUpdateProductOrderResult = productOrderController
                .updateProductOrder(1L, new ProductOrderDtoRequest(orderDate, new HashSet<>(), "Hospital Service Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(productOrderRepository).findById(Mockito.<Long>any());
        verify(productOrderRepository).save(Mockito.<ProductOrder>any());
        ProductOrderDtoResponse body = actualUpdateProductOrderResult.getBody();
        assertEquals("00:00", body.orderDate().toLocalTime().toString());
        assertEquals("1", body.code());
        assertEquals("Name", body.hospitalServiceName());
        assertEquals(200, actualUpdateProductOrderResult.getStatusCodeValue());
        assertTrue(body.deliveryNotes().isEmpty());
        assertTrue(actualUpdateProductOrderResult.hasBody());
        assertTrue(actualUpdateProductOrderResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link ProductOrderController#deleteProductOrder(Long)}
     */
    @Test
    void testDeleteProductOrder() throws Exception {
        doNothing().when(productOrderService).deleteProductOrder(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/productOrders/{code}", 1L);
        MockMvcBuilders.standaloneSetup(productOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Product Order deleted successfully"));
    }

    /**
     * Method under test: {@link ProductOrderController#deleteProductOrder(Long)}
     */
    @Test
    void testDeleteProductOrder2() throws Exception {
        doNothing().when(productOrderService).deleteProductOrder(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/productOrders/{code}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(productOrderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Product Order deleted successfully"));
    }
}
