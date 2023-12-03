package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.VacationDtoRequest;
import com.ghopital.projet.dto.response.VacationDtoResponse;
import com.ghopital.projet.entity.Diploma;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.entity.Vacation;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.VacationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {VacationServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class VacationServiceImplTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private VacationRepository vacationRepository;

    @Autowired
    private VacationServiceImpl vacationServiceImpl;

    /**
     * Method under test:
     * {@link VacationServiceImpl#createVacation(Long, VacationDtoRequest)}
     */
    @Test
    void testCreateVacation() {
        Vacation vacation = new Vacation();
        vacation.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        vacation.setId(1L);
        vacation.setSickVacation(true);
        vacation.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
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
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        VacationDtoResponse actualCreateVacationResult = vacationServiceImpl.createVacation(1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(vacationRepository).save(Mockito.<Vacation>any());
        assertEquals("00:00", actualCreateVacationResult.startDate().toLocalTime().toString());
        assertEquals("1970-01-01", actualCreateVacationResult.endDate().toLocalDate().toString());
        assertNull(actualCreateVacationResult.employeeId());
        assertEquals(1L, actualCreateVacationResult.id().longValue());
        assertTrue(actualCreateVacationResult.isSickVacation());
    }

    /**
     * Method under test:
     * {@link VacationServiceImpl#createVacation(Long, VacationDtoRequest)}
     */
    @Test
    void testCreateVacation2() {
        Vacation vacation = new Vacation();
        vacation.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        vacation.setId(1L);
        vacation.setSickVacation(true);
        vacation.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
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
        when(employeeRepository.save(Mockito.<Employee>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(AppException.class, () -> vacationServiceImpl.createVacation(1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true)));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(vacationRepository).save(Mockito.<Vacation>any());
    }

    /**
     * Method under test:
     * {@link VacationServiceImpl#createVacation(Long, VacationDtoRequest)}
     */
    @Test
    void testCreateVacation3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.createVacation(1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true)));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link VacationServiceImpl#getVacationById(Long, Long)}
     */
    @Test
    void testGetVacationById() {
        when(vacationRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));

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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class, () -> vacationServiceImpl.getVacationById(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link VacationServiceImpl#getVacationById(Long, Long)}
     */
    @Test
    void testGetVacationById2() {
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        VacationDtoResponse actualVacationById = vacationServiceImpl.getVacationById(1L, 1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
        assertEquals("00:00", actualVacationById.startDate().toLocalTime().toString());
        assertEquals("1970-01-01", actualVacationById.endDate().toLocalDate().toString());
        assertEquals(1L, actualVacationById.employeeId().longValue());
        assertEquals(1L, actualVacationById.id().longValue());
        assertTrue(actualVacationById.isSickVacation());
    }

    /**
     * Method under test: {@link VacationServiceImpl#getVacationById(Long, Long)}
     */
    @Test
    void testGetVacationById3() {
        Optional<Vacation> emptyResult = Optional.empty();
        when(vacationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.getVacationById(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link VacationServiceImpl#getVacationById(Long, Long)}
     */
    @Test
    void testGetVacationById4() {
        Vacation vacation = new Vacation();
        vacation.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        vacation.setId(1L);
        vacation.setSickVacation(true);
        vacation.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<Vacation> ofResult = Optional.of(vacation);
        when(vacationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.getVacationById(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link VacationServiceImpl#getAllVacations()}
     */
    @Test
    void testGetAllVacations() {
        when(vacationRepository.findAll()).thenReturn(new ArrayList<>());
        List<VacationDtoResponse> actualAllVacations = vacationServiceImpl.getAllVacations();
        verify(vacationRepository).findAll();
        assertTrue(actualAllVacations.isEmpty());
    }

    /**
     * Method under test:  {@link VacationServiceImpl#getAllVacations()}
     */
    @Test
    void testGetAllVacations2() {
        when(vacationRepository.findAll()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> vacationServiceImpl.getAllVacations());
        verify(vacationRepository).findAll();
    }

    /**
     * Method under test: {@link VacationServiceImpl#getEmployeeVacations(Long)}
     */
    @Test
    void testGetEmployeeVacations() {
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        List<VacationDtoResponse> actualEmployeeVacations = vacationServiceImpl.getEmployeeVacations(1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        assertTrue(actualEmployeeVacations.isEmpty());
    }

    /**
     * Method under test: {@link VacationServiceImpl#getEmployeeVacations(Long)}
     */
    @Test
    void testGetEmployeeVacations2() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.getEmployeeVacations(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link VacationServiceImpl#getEmployeeVacations(Long)}
     */
    @Test
    void testGetEmployeeVacations3() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> vacationServiceImpl.getEmployeeVacations(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link VacationServiceImpl#updateVacation(Long, Long, VacationDtoRequest)}
     */
    @Test
    void testUpdateVacation() {
        when(vacationRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));

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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(AppException.class, () -> vacationServiceImpl.updateVacation(1L, 1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true)));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link VacationServiceImpl#updateVacation(Long, Long, VacationDtoRequest)}
     */
    @Test
    void testUpdateVacation2() {
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        VacationDtoResponse actualUpdateVacationResult = vacationServiceImpl.updateVacation(1L, 1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).save(Mockito.<Vacation>any());
        assertEquals("00:00", actualUpdateVacationResult.startDate().toLocalTime().toString());
        assertEquals("1970-01-01", actualUpdateVacationResult.endDate().toLocalDate().toString());
        assertNull(actualUpdateVacationResult.employeeId());
        assertEquals(1L, actualUpdateVacationResult.id().longValue());
        assertTrue(actualUpdateVacationResult.isSickVacation());
    }

    /**
     * Method under test:
     * {@link VacationServiceImpl#updateVacation(Long, Long, VacationDtoRequest)}
     */
    @Test
    void testUpdateVacation3() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(-1L);
        when(employee.getVacations()).thenReturn(new HashSet<>());
        doNothing().when(employee).setCin(Mockito.<String>any());
        doNothing().when(employee).setDiplomas(Mockito.<Set<Diploma>>any());
        doNothing().when(employee).setDocuments(Mockito.<Set<File>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setId(Mockito.<Long>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setRecruitmentDate(Mockito.<LocalDate>any());
        doNothing().when(employee).setRegistrationNumber(Mockito.<String>any());
        doNothing().when(employee).setService(Mockito.<HospitalService>any());
        doNothing().when(employee).setVacations(Mockito.<Set<Vacation>>any());
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(AppException.class, () -> vacationServiceImpl.updateVacation(1L, 1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true)));
        verify(employee).getId();
        verify(employee, atLeast(1)).getVacations();
        verify(employee).setCin(Mockito.<String>any());
        verify(employee).setDiplomas(Mockito.<Set<Diploma>>any());
        verify(employee).setDocuments(Mockito.<Set<File>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setId(Mockito.<Long>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setRecruitmentDate(Mockito.<LocalDate>any());
        verify(employee).setRegistrationNumber(Mockito.<String>any());
        verify(employee).setService(Mockito.<HospitalService>any());
        verify(employee).setVacations(Mockito.<Set<Vacation>>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link VacationServiceImpl#updateVacation(Long, Long, VacationDtoRequest)}
     */
    @Test
    void testUpdateVacation4() {
        Optional<Vacation> emptyResult = Optional.empty();
        when(vacationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.updateVacation(1L, 1L,
                new VacationDtoRequest(startDate, LocalDate.of(1970, 1, 1).atStartOfDay(), true)));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link VacationServiceImpl#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation() {
        when(vacationRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));

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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class, () -> vacationServiceImpl.deleteVacation(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link VacationServiceImpl#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation2() {
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
        doNothing().when(vacationRepository).delete(Mockito.<Vacation>any());
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        vacationServiceImpl.deleteVacation(1L, 1L);
        verify(vacationRepository).delete(Mockito.<Vacation>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link VacationServiceImpl#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation3() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(-1L);
        when(employee.getVacations()).thenReturn(new HashSet<>());
        doNothing().when(employee).setCin(Mockito.<String>any());
        doNothing().when(employee).setDiplomas(Mockito.<Set<Diploma>>any());
        doNothing().when(employee).setDocuments(Mockito.<Set<File>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setId(Mockito.<Long>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setRecruitmentDate(Mockito.<LocalDate>any());
        doNothing().when(employee).setRegistrationNumber(Mockito.<String>any());
        doNothing().when(employee).setService(Mockito.<HospitalService>any());
        doNothing().when(employee).setVacations(Mockito.<Set<Vacation>>any());
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> vacationServiceImpl.deleteVacation(1L, 1L));
        verify(employee).getId();
        verify(employee, atLeast(1)).getVacations();
        verify(employee).setCin(Mockito.<String>any());
        verify(employee).setDiplomas(Mockito.<Set<Diploma>>any());
        verify(employee).setDocuments(Mockito.<Set<File>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setId(Mockito.<Long>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setRecruitmentDate(Mockito.<LocalDate>any());
        verify(employee).setRegistrationNumber(Mockito.<String>any());
        verify(employee).setService(Mockito.<HospitalService>any());
        verify(employee).setVacations(Mockito.<Set<Vacation>>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link VacationServiceImpl#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation4() {
        Optional<Vacation> emptyResult = Optional.empty();
        when(vacationRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.deleteVacation(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(vacationRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link VacationServiceImpl#deleteVacation(Long, Long)}
     */
    @Test
    void testDeleteVacation5() {
        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Employee employee = mock(Employee.class);
        when(employee.getVacations()).thenReturn(new HashSet<>());
        doNothing().when(employee).setCin(Mockito.<String>any());
        doNothing().when(employee).setDiplomas(Mockito.<Set<Diploma>>any());
        doNothing().when(employee).setDocuments(Mockito.<Set<File>>any());
        doNothing().when(employee).setFirstName(Mockito.<String>any());
        doNothing().when(employee).setId(Mockito.<Long>any());
        doNothing().when(employee).setLastName(Mockito.<String>any());
        doNothing().when(employee).setRecruitmentDate(Mockito.<LocalDate>any());
        doNothing().when(employee).setRegistrationNumber(Mockito.<String>any());
        doNothing().when(employee).setService(Mockito.<HospitalService>any());
        doNothing().when(employee).setVacations(Mockito.<Set<Vacation>>any());
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
        when(vacationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> vacationServiceImpl.deleteVacation(1L, 1L));
        verify(employee, atLeast(1)).getVacations();
        verify(employee).setCin(Mockito.<String>any());
        verify(employee).setDiplomas(Mockito.<Set<Diploma>>any());
        verify(employee).setDocuments(Mockito.<Set<File>>any());
        verify(employee).setFirstName(Mockito.<String>any());
        verify(employee).setId(Mockito.<Long>any());
        verify(employee).setLastName(Mockito.<String>any());
        verify(employee).setRecruitmentDate(Mockito.<LocalDate>any());
        verify(employee).setRegistrationNumber(Mockito.<String>any());
        verify(employee).setService(Mockito.<HospitalService>any());
        verify(employee).setVacations(Mockito.<Set<Vacation>>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }
}
