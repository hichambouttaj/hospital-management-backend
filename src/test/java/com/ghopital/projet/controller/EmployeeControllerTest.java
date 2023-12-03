package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.EmployeeDtoRequest;
import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.EmployeeDtoResponse;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.repository.ServiceRepository;
import com.ghopital.projet.service.EmployeeService;
import com.ghopital.projet.service.impl.EmployeeServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {EmployeeController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {
    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;

    /**
     * Method under test:  {@link EmployeeController#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(Mockito.<Long>any()))
                .thenReturn(new EmployeeDtoResponse(1L, "Jane", "Doe", "Cin", "42", LocalDate.of(1970, 1, 1), "Service Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/employees/{employeeId}", 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"cin\":\"Cin\",\"registrationNumber\":\"42\",\"recruitmentDate\":"
                                        + "[1970,1,1],\"serviceName\":\"Service Name\"}"));
    }

    /**
     * Method under test:  {@link EmployeeController#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/employees");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link EmployeeController#updateEmployee(Long, EmployeeDtoRequest)}
     */
    @Test
    void testUpdateEmployee() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.EmployeeDtoRequest["recruitmentDate"])
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

        Employee employee = new Employee();
        employee.setCin("Cin");
        employee.setDiplomas(new HashSet<>());
        employee.setDocuments(new HashSet<>());
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        employee.setRecruitmentDate(LocalDate.of(1970, 1, 1));
        employee.setRegistrationNumber("42");
        employee.setService(hospitalService);
        employee.setVacations(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());

        Employee employee2 = new Employee();
        employee2.setCin("Cin");
        employee2.setDiplomas(new HashSet<>());
        employee2.setDocuments(new HashSet<>());
        employee2.setFirstName("Jane");
        employee2.setId(1L);
        employee2.setLastName("Doe");
        employee2.setRecruitmentDate(LocalDate.of(1970, 1, 1));
        employee2.setRegistrationNumber("42");
        employee2.setService(hospitalService2);
        employee2.setVacations(new HashSet<>());
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        ServiceRepository serviceRepository = mock(ServiceRepository.class);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        EmployeeController employeeController = new EmployeeController(
                new EmployeeServiceImpl(employeeRepository, serviceRepository, mock(FileRepository.class)));
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        ResponseEntity<EmployeeDtoResponse> actualUpdateEmployeeResult = employeeController.updateEmployee(1L,
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        EmployeeDtoResponse body = actualUpdateEmployeeResult.getBody();
        assertEquals("1970-01-01", body.recruitmentDate().toString());
        assertEquals("42", body.registrationNumber());
        assertEquals("Cin", body.cin());
        assertEquals("Doe", body.lastName());
        assertEquals("Jane", body.firstName());
        assertEquals("Name", body.serviceName());
        assertEquals(1L, body.id().longValue());
        assertEquals(200, actualUpdateEmployeeResult.getStatusCodeValue());
        assertTrue(actualUpdateEmployeeResult.hasBody());
        assertTrue(actualUpdateEmployeeResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:  {@link EmployeeController#getDocumentsOfEmployee(Long)}
     */
    @Test
    void testGetDocumentsOfEmployee() throws Exception {
        when(employeeService.getDocumentsOfEmployee(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}/documents", 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link EmployeeController#addDocumentToEmployee(Long, Long)}
     */
    @Test
    void testAddDocumentToEmployee() throws Exception {
        when(employeeService.addDocumentToEmployee(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new EmployeeDtoResponse(1L, "Jane", "Doe", "Cin", "42", LocalDate.of(1970, 1, 1), "Service Name"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/employees/{employeeId}/documents/{documentId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"cin\":\"Cin\",\"registrationNumber\":\"42\",\"recruitmentDate\":"
                                        + "[1970,1,1],\"serviceName\":\"Service Name\"}"));
    }

    /**
     * Method under test:
     * {@link EmployeeController#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee() throws Exception {
        doNothing().when(employeeService).deleteDocumentOfEmployee(Mockito.<Long>any(), Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}/documents/{documentId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Document deleted successfully"));
    }

    /**
     * Method under test:
     * {@link EmployeeController#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee2() throws Exception {
        doNothing().when(employeeService).deleteDocumentOfEmployee(Mockito.<Long>any(), Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}/documents/{documentId}", 1L, 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Document deleted successfully"));
    }

    /**
     * Method under test:  {@link EmployeeController#addServiceToEmployee(Long, ServiceDtoRequest)}
     */
    @Test
    void testAddServiceToEmployee() throws Exception {
        when(employeeService.addServiceToEmployee(Mockito.<Long>any(), Mockito.<ServiceDtoRequest>any()))
                .thenReturn(new EmployeeDtoResponse(1L, "Jane", "Doe", "Cin", "42", LocalDate.of(1970, 1, 1), "Service Name"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .put("/api/v1/employees/{employeeId}/services", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ServiceDtoRequest("Name")));
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"cin\":\"Cin\",\"registrationNumber\":\"42\",\"recruitmentDate\":"
                                        + "[1970,1,1],\"serviceName\":\"Service Name\"}"));
    }

    /**
     * Method under test: {@link EmployeeController#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/employees/{employeeId}", 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee deleted successfully"));
    }

    /**
     * Method under test: {@link EmployeeController#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee2() throws Exception {
        doNothing().when(employeeService).deleteEmployee(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/employees/{employeeId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee deleted successfully"));
    }

    /**
     * Method under test:  {@link EmployeeController#createEmployee(EmployeeDtoRequest)}
     */
    @Test
    void testCreateEmployee() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", null, new ServiceDtoRequest("Name"))));
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
