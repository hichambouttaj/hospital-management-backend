package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.VacationDtoRequest;
import com.ghopital.projet.dto.response.VacationDtoResponse;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.entity.Vacation;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.VacationRepository;
import com.ghopital.projet.service.VacationService;
import com.ghopital.projet.service.impl.VacationServiceImpl;

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

@ContextConfiguration(classes = {VacationController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class VacationControllerTest {
    @Autowired
    private VacationController vacationController;

    @MockBean
    private VacationService vacationService;

    /**
     * Method under test:
     * {@link VacationController#createVacation(Long, VacationDtoRequest)}
     */
    @Test
    void testCreateVacation() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.VacationDtoRequest["startDate"])
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

        Vacation vacation = new Vacation();
        vacation.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        vacation.setId(1L);
        vacation.setSickVacation(true);
        vacation.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        VacationRepository vacationRepository = mock(VacationRepository.class);
        when(vacationRepository.save(Mockito.<Vacation>any())).thenReturn(vacation);

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
        VacationController vacationController = new VacationController(
                new VacationServiceImpl(vacationRepository, employeeRepository));
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<VacationDtoResponse> actualCreateVacationResult = vacationController.createVacation(1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(vacationRepository).save(Mockito.<Vacation>any());
        VacationDtoResponse body = actualCreateVacationResult.getBody();
        assertEquals("00:00", body.endDate().toLocalTime().toString());
        assertEquals("00:00", body.startDate().toLocalTime().toString());
        assertNull(body.employeeId());
        assertEquals(1L, body.id().longValue());
        assertEquals(201, actualCreateVacationResult.getStatusCodeValue());
        assertTrue(body.isSickVacation());
        assertTrue(actualCreateVacationResult.hasBody());
        assertTrue(actualCreateVacationResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link VacationController#getVacationById(Long, Long)}
     */
    @Test
    void testGetVacationById() throws Exception {
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(vacationService.getVacationById(Mockito.<Long>any(), Mockito.<Long>any()))
                .thenReturn(new VacationDtoResponse(1L, startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true, 1L));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}/vacations/{vacationId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(vacationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"startDate\":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0],\"isSickVacation\":true,\"employeeId\":1}"));
    }

    /**
     * Method under test:
     * {@link VacationController#updateVacation(Long, Long, VacationDtoRequest)}
     */
    @Test
    void testUpdateVacation() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.ghopital.projet.dto.request.VacationDtoRequest["startDate"])
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

        Vacation vacation = new Vacation();
        vacation.setEmployee(employee);
        vacation.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        vacation.setId(1L);
        vacation.setSickVacation(true);
        vacation.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<Vacation> ofResult = Optional.of(vacation);

        Vacation vacation2 = new Vacation();
        vacation2.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        vacation2.setId(1L);
        vacation2.setSickVacation(true);
        vacation2.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        VacationRepository vacationRepository = mock(VacationRepository.class);
        when(vacationRepository.save(Mockito.<Vacation>any())).thenReturn(vacation2);
        when(vacationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        VacationController vacationController = new VacationController(
                new VacationServiceImpl(vacationRepository, employeeRepository));
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        ResponseEntity<VacationDtoResponse> actualUpdateVacationResult = vacationController.updateVacation(1L, 1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).save(Mockito.<Vacation>any());
        VacationDtoResponse body = actualUpdateVacationResult.getBody();
        assertEquals("00:00", body.endDate().toLocalTime().toString());
        assertEquals("00:00", body.startDate().toLocalTime().toString());
        assertNull(body.employeeId());
        assertEquals(1L, body.id().longValue());
        assertEquals(200, actualUpdateVacationResult.getStatusCodeValue());
        assertTrue(body.isSickVacation());
        assertTrue(actualUpdateVacationResult.hasBody());
        assertTrue(actualUpdateVacationResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link VacationController#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation() throws Exception {
        doNothing().when(vacationService).deleteVacation(Mockito.<Long>any(), Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}/vacations/{vacationId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(vacationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Vacation deleted successfully"));
    }

    /**
     * Method under test: {@link VacationController#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation2() throws Exception {
        doNothing().when(vacationService).deleteVacation(Mockito.<Long>any(), Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/employees/{employeeId}/vacations/{vacationId}", 1L, 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(vacationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Vacation deleted successfully"));
    }

    /**
     * Method under test:  {@link VacationController#getVacationsForEmployee(Long)}
     */
    @Test
    void testGetVacationsForEmployee() throws Exception {
        when(vacationService.getEmployeeVacations(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}/vacations", 1L);
        MockMvcBuilders.standaloneSetup(vacationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
