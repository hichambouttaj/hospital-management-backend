package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.DiplomaDtoRequest;
import com.ghopital.projet.dto.response.DiplomaDtoResponse;
import com.ghopital.projet.entity.Diploma;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.repository.DiplomaRepository;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.service.DiplomaService;
import com.ghopital.projet.service.impl.DiplomaServiceImpl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
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

@ContextConfiguration(classes = {DiplomaController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class DiplomaControllerTest {
    @Autowired
    private DiplomaController diplomaController;

    @MockBean
    private DiplomaService diplomaService;

    /**
     * Method under test:
     * {@link DiplomaController#createDiploma(Long, DiplomaDtoRequest)}
     */
    @Test
    void testCreateDiploma() throws UnsupportedEncodingException {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.DiplomaDtoRequest["startDate"])
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

        File document = new File();
        document.setData("AXAXAXAX".getBytes("UTF-8"));
        document.setId(1L);
        document.setName("Name");
        document.setType("Type");

        Diploma diploma = new Diploma();
        diploma.setDocument(document);
        diploma.setEndDate(LocalDate.of(1970, 1, 1));
        diploma.setId(1L);
        diploma.setStartDate(LocalDate.of(1970, 1, 1));
        diploma.setTitle("Dr");
        DiplomaRepository diplomaRepository = mock(DiplomaRepository.class);
        when(diplomaRepository.save(Mockito.<Diploma>any())).thenReturn(diploma);

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
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        DiplomaController diplomaController = new DiplomaController(
                new DiplomaServiceImpl(diplomaRepository, mock(FileRepository.class), employeeRepository));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        ResponseEntity<DiplomaDtoResponse> actualCreateDiplomaResult = diplomaController.createDiploma(1L,
                new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1)));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
        DiplomaDtoResponse body = actualCreateDiplomaResult.getBody();
        assertEquals("1970-01-01", body.endDate().toString());
        assertEquals("1970-01-01", body.startDate().toString());
        assertEquals("Dr", body.title());
        assertNull(body.employeeId());
        assertEquals(1L, body.documentId().longValue());
        assertEquals(1L, body.id().longValue());
        assertEquals(201, actualCreateDiplomaResult.getStatusCodeValue());
        assertTrue(actualCreateDiplomaResult.hasBody());
        assertTrue(actualCreateDiplomaResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link DiplomaController#getDiplomaById(Long, Long)}
     */
    @Test
    void testGetDiplomaById() throws Exception {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        when(diplomaService.getDiplomaById(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new DiplomaDtoResponse(1L, "Dr", startDate, LocalDate.of(1970, 1, 1), 1L, 1L));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}/diplomas/{diplomaId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(diplomaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"title\":\"Dr\",\"startDate\":[1970,1,1],\"endDate\":[1970,1,1],\"documentId\":1,\"employeeId\":1}"));
    }

    /**
     * Method under test:
     * {@link DiplomaController#updateDiploma(Long, Long, DiplomaDtoRequest)}
     */
    @Test
    void testUpdateDiploma() throws UnsupportedEncodingException {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.DiplomaDtoRequest["startDate"])
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

        File document = new File();
        document.setData("AXAXAXAX".getBytes("UTF-8"));
        document.setId(1L);
        document.setName("Name");
        document.setType("Type");

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

        Diploma diploma = new Diploma();
        diploma.setEmployee(employee);
        diploma.setDocument(document);
        diploma.setEndDate(LocalDate.of(1970, 1, 1));
        diploma.setId(1L);
        diploma.setStartDate(LocalDate.of(1970, 1, 1));
        diploma.setTitle("Dr");
        Optional<Diploma> ofResult = Optional.of(diploma);

        File document2 = new File();
        document2.setData("AXAXAXAX".getBytes("UTF-8"));
        document2.setId(1L);
        document2.setName("Name");
        document2.setType("Type");

        Diploma diploma2 = new Diploma();
        diploma2.setDocument(document2);
        diploma2.setEndDate(LocalDate.of(1970, 1, 1));
        diploma2.setId(1L);
        diploma2.setStartDate(LocalDate.of(1970, 1, 1));
        diploma2.setTitle("Dr");
        DiplomaRepository diplomaRepository = mock(DiplomaRepository.class);
        when(diplomaRepository.save(Mockito.<Diploma>any())).thenReturn(diploma2);
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        Optional<Employee> ofResult2 = Optional.of(employee2);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        DiplomaController diplomaController = new DiplomaController(
                new DiplomaServiceImpl(diplomaRepository, mock(FileRepository.class), employeeRepository));
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        ResponseEntity<DiplomaDtoResponse> actualUpdateDiplomaResult = diplomaController.updateDiploma(1L, 1L,
                new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1)));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
        DiplomaDtoResponse body = actualUpdateDiplomaResult.getBody();
        assertEquals("1970-01-01", body.endDate().toString());
        assertEquals("1970-01-01", body.startDate().toString());
        assertEquals("Dr", body.title());
        assertNull(body.employeeId());
        assertEquals(1L, body.documentId().longValue());
        assertEquals(1L, body.id().longValue());
        assertEquals(200, actualUpdateDiplomaResult.getStatusCodeValue());
        assertTrue(actualUpdateDiplomaResult.hasBody());
        assertTrue(actualUpdateDiplomaResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link DiplomaController#addDocumentToDiploma(Long, Long, Long)}
     */
    @Test
    void testAddDocumentToDiploma() throws Exception {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        when(diplomaService.addDocumentToDiploma(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new DiplomaDtoResponse(1L, "Dr", startDate, LocalDate.of(1970, 1, 1), 1L, 1L));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/employees/{employeeId}/diplomas/{diplomaId}/document/{documentId}", 1L, 1L, 1L);
        MockMvcBuilders.standaloneSetup(diplomaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"title\":\"Dr\",\"startDate\":[1970,1,1],\"endDate\":[1970,1,1],\"documentId\":1,\"employeeId\":1}"));
    }

    /**
     * Method under test: {@link DiplomaController#deleteDiploma(Long, Long)}
     */
    @Test
    void testDeleteDiploma() throws Exception {
        doNothing().when(diplomaService).deleteDiploma(Mockito.<Long>any(), Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}/diplomas/{diplomaId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(diplomaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Diploma deleted successfully"));
    }

    /**
     * Method under test: {@link DiplomaController#deleteDiploma(Long, Long)}
     */
    @Test
    void testDeleteDiploma2() throws Exception {
        doNothing().when(diplomaService).deleteDiploma(Mockito.<Long>any(), Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}/diplomas/{diplomaId}", 1L, 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(diplomaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Diploma deleted successfully"));
    }

    /**
     * Method under test:  {@link DiplomaController#getDiplomasForEmployee(Long)}
     */
    @Test
    void testGetDiplomasForEmployee() throws Exception {
        when(diplomaService.getEmployeeDiplomas(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/employees/{employeeId}/diplomas",
                1L);
        MockMvcBuilders.standaloneSetup(diplomaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
