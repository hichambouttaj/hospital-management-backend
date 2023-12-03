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

import com.ghopital.projet.dto.request.DiplomaDtoRequest;
import com.ghopital.projet.dto.response.DiplomaDtoResponse;
import com.ghopital.projet.entity.Diploma;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.entity.Vacation;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.DiplomaRepository;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.FileRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
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

@ContextConfiguration(classes = {DiplomaServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class DiplomaServiceImplTest {
    @MockBean
    private DiplomaRepository diplomaRepository;

    @Autowired
    private DiplomaServiceImpl diplomaServiceImpl;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private FileRepository fileRepository;

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#createDiploma(Long, DiplomaDtoRequest)}
     */
    @Test
    void testCreateDiploma() throws UnsupportedEncodingException {
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        DiplomaDtoResponse actualCreateDiplomaResult = diplomaServiceImpl.createDiploma(1L,
                new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1)));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
        assertEquals("1970-01-01", actualCreateDiplomaResult.endDate().toString());
        assertEquals("1970-01-01", actualCreateDiplomaResult.startDate().toString());
        assertEquals("Dr", actualCreateDiplomaResult.title());
        assertNull(actualCreateDiplomaResult.employeeId());
        assertEquals(1L, actualCreateDiplomaResult.documentId().longValue());
        assertEquals(1L, actualCreateDiplomaResult.id().longValue());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#createDiploma(Long, DiplomaDtoRequest)}
     */
    @Test
    void testCreateDiploma2() {
        when(diplomaRepository.save(Mockito.<Diploma>any()))
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
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(AppException.class,
                () -> diplomaServiceImpl.createDiploma(1L, new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1))));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
    }

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#createDiploma(Long, DiplomaDtoRequest)}
     */
    @Test
    void testCreateDiploma3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(ResourceNotFoundException.class,
                () -> diplomaServiceImpl.createDiploma(1L, new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1))));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#getDiplomaById(Long, Long)}
     */
    @Test
    void testGetDiplomaById() {
        when(diplomaRepository.findById(Mockito.<Long>any()))
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
        assertThrows(AppException.class, () -> diplomaServiceImpl.getDiplomaById(1L, 1L));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#getDiplomaById(Long, Long)}
     */
    @Test
    void testGetDiplomaById2() throws UnsupportedEncodingException {
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        DiplomaDtoResponse actualDiplomaById = diplomaServiceImpl.getDiplomaById(1L, 1L);
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        assertEquals("1970-01-01", actualDiplomaById.endDate().toString());
        assertEquals("1970-01-01", actualDiplomaById.startDate().toString());
        assertEquals("Dr", actualDiplomaById.title());
        assertEquals(1L, actualDiplomaById.documentId().longValue());
        assertEquals(1L, actualDiplomaById.employeeId().longValue());
        assertEquals(1L, actualDiplomaById.id().longValue());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#getDiplomaById(Long, Long)}
     */
    @Test
    void testGetDiplomaById3() {
        Optional<Diploma> emptyResult = Optional.empty();
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

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
        assertThrows(ResourceNotFoundException.class, () -> diplomaServiceImpl.getDiplomaById(1L, 1L));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#getDiplomaById(Long, Long)}
     */
    @Test
    void testGetDiplomaById4() throws UnsupportedEncodingException {
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
        Optional<Diploma> ofResult = Optional.of(diploma);
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> diplomaServiceImpl.getDiplomaById(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#getAllDiploma()}
     */
    @Test
    void testGetAllDiploma() {
        when(diplomaRepository.findAll()).thenReturn(new ArrayList<>());
        List<DiplomaDtoResponse> actualAllDiploma = diplomaServiceImpl.getAllDiploma();
        verify(diplomaRepository).findAll();
        assertTrue(actualAllDiploma.isEmpty());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#getAllDiploma()}
     */
    @Test
    void testGetAllDiploma2() {
        when(diplomaRepository.findAll()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> diplomaServiceImpl.getAllDiploma());
        verify(diplomaRepository).findAll();
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#getEmployeeDiplomas(Long)}
     */
    @Test
    void testGetEmployeeDiplomas() {
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
        List<DiplomaDtoResponse> actualEmployeeDiplomas = diplomaServiceImpl.getEmployeeDiplomas(1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        assertTrue(actualEmployeeDiplomas.isEmpty());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#getEmployeeDiplomas(Long)}
     */
    @Test
    void testGetEmployeeDiplomas2() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> diplomaServiceImpl.getEmployeeDiplomas(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#getEmployeeDiplomas(Long)}
     */
    @Test
    void testGetEmployeeDiplomas3() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> diplomaServiceImpl.getEmployeeDiplomas(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#updateDiploma(Long, Long, DiplomaDtoRequest)}
     */
    @Test
    void testUpdateDiploma() {
        when(diplomaRepository.findById(Mockito.<Long>any()))
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
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(AppException.class, () -> diplomaServiceImpl.updateDiploma(1L, 1L,
                new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1))));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#updateDiploma(Long, Long, DiplomaDtoRequest)}
     */
    @Test
    void testUpdateDiploma2() throws UnsupportedEncodingException {
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        DiplomaDtoResponse actualUpdateDiplomaResult = diplomaServiceImpl.updateDiploma(1L, 1L,
                new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1)));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
        assertEquals("1970-01-01", actualUpdateDiplomaResult.endDate().toString());
        assertEquals("1970-01-01", actualUpdateDiplomaResult.startDate().toString());
        assertEquals("Dr", actualUpdateDiplomaResult.title());
        assertNull(actualUpdateDiplomaResult.employeeId());
        assertEquals(1L, actualUpdateDiplomaResult.documentId().longValue());
        assertEquals(1L, actualUpdateDiplomaResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#updateDiploma(Long, Long, DiplomaDtoRequest)}
     */
    @Test
    void testUpdateDiploma3() throws UnsupportedEncodingException {
        File document = new File();
        document.setData("AXAXAXAX".getBytes("UTF-8"));
        document.setId(1L);
        document.setName("Name");
        document.setType("Type");

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(-1L);
        when(employee.getDiplomas()).thenReturn(new HashSet<>());
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

        Diploma diploma = new Diploma();
        diploma.setEmployee(employee);
        diploma.setDocument(document);
        diploma.setEndDate(LocalDate.of(1970, 1, 1));
        diploma.setId(1L);
        diploma.setStartDate(LocalDate.of(1970, 1, 1));
        diploma.setTitle("Dr");
        Optional<Diploma> ofResult = Optional.of(diploma);
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        assertThrows(AppException.class, () -> diplomaServiceImpl.updateDiploma(1L, 1L,
                new DiplomaDtoRequest("Dr", startDate, LocalDate.of(1970, 1, 1))));
        verify(employee, atLeast(1)).getDiplomas();
        verify(employee).getId();
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
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#addDocumentToDiploma(Long, Long, Long)}
     */
    @Test
    void testAddDocumentToDiploma() {
        when(diplomaRepository.findById(Mockito.<Long>any()))
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
        assertThrows(AppException.class, () -> diplomaServiceImpl.addDocumentToDiploma(1L, 1L, 1L));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#addDocumentToDiploma(Long, Long, Long)}
     */
    @Test
    void testAddDocumentToDiploma2() throws UnsupportedEncodingException {
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
        when(diplomaRepository.save(Mockito.<Diploma>any())).thenReturn(diploma2);
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult2 = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

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
        Optional<Employee> ofResult3 = Optional.of(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        DiplomaDtoResponse actualAddDocumentToDiplomaResult = diplomaServiceImpl.addDocumentToDiploma(1L, 1L, 1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
        assertEquals("1970-01-01", actualAddDocumentToDiplomaResult.endDate().toString());
        assertEquals("1970-01-01", actualAddDocumentToDiplomaResult.startDate().toString());
        assertEquals("Dr", actualAddDocumentToDiplomaResult.title());
        assertNull(actualAddDocumentToDiplomaResult.employeeId());
        assertEquals(1L, actualAddDocumentToDiplomaResult.documentId().longValue());
        assertEquals(1L, actualAddDocumentToDiplomaResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#addDocumentToDiploma(Long, Long, Long)}
     */
    @Test
    void testAddDocumentToDiploma3() throws UnsupportedEncodingException {
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
        when(diplomaRepository.save(Mockito.<Diploma>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult2 = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

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
        Optional<Employee> ofResult3 = Optional.of(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        assertThrows(AppException.class, () -> diplomaServiceImpl.addDocumentToDiploma(1L, 1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(diplomaRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(diplomaRepository).save(Mockito.<Diploma>any());
    }

    /**
     * Method under test:
     * {@link DiplomaServiceImpl#addDocumentToDiploma(Long, Long, Long)}
     */
    @Test
    void testAddDocumentToDiploma4() throws UnsupportedEncodingException {
        File document = new File();
        document.setData("AXAXAXAX".getBytes("UTF-8"));
        document.setId(1L);
        document.setName("Name");
        document.setType("Type");

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(-1L);
        when(employee.getDiplomas()).thenReturn(new HashSet<>());
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

        Diploma diploma = new Diploma();
        diploma.setEmployee(employee);
        diploma.setDocument(document);
        diploma.setEndDate(LocalDate.of(1970, 1, 1));
        diploma.setId(1L);
        diploma.setStartDate(LocalDate.of(1970, 1, 1));
        diploma.setTitle("Dr");
        Optional<Diploma> ofResult = Optional.of(diploma);
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult2 = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

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
        Optional<Employee> ofResult3 = Optional.of(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        assertThrows(AppException.class, () -> diplomaServiceImpl.addDocumentToDiploma(1L, 1L, 1L));
        verify(employee, atLeast(1)).getDiplomas();
        verify(employee).getId();
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
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link DiplomaServiceImpl#deleteDiploma(Long, Long)}
     */
    @Test
    void testDeleteDiploma() {
        when(diplomaRepository.findById(Mockito.<Long>any()))
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
        assertThrows(AppException.class, () -> diplomaServiceImpl.deleteDiploma(1L, 1L));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#deleteDiploma(Long, Long)}
     */
    @Test
    void testDeleteDiploma2() throws UnsupportedEncodingException {
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
        doNothing().when(diplomaRepository).delete(Mockito.<Diploma>any());
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        diplomaServiceImpl.deleteDiploma(1L, 1L);
        verify(diplomaRepository).delete(Mockito.<Diploma>any());
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#deleteDiploma(Long, Long)}
     */
    @Test
    void testDeleteDiploma3() throws UnsupportedEncodingException {
        File document = new File();
        document.setData("AXAXAXAX".getBytes("UTF-8"));
        document.setId(1L);
        document.setName("Name");
        document.setType("Type");

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(-1L);
        when(employee.getDiplomas()).thenReturn(new HashSet<>());
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

        Diploma diploma = new Diploma();
        diploma.setEmployee(employee);
        diploma.setDocument(document);
        diploma.setEndDate(LocalDate.of(1970, 1, 1));
        diploma.setId(1L);
        diploma.setStartDate(LocalDate.of(1970, 1, 1));
        diploma.setTitle("Dr");
        Optional<Diploma> ofResult = Optional.of(diploma);
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
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> diplomaServiceImpl.deleteDiploma(1L, 1L));
        verify(employee, atLeast(1)).getDiplomas();
        verify(employee).getId();
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
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DiplomaServiceImpl#deleteDiploma(Long, Long)}
     */
    @Test
    void testDeleteDiploma4() {
        Optional<Diploma> emptyResult = Optional.empty();
        when(diplomaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

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
        assertThrows(ResourceNotFoundException.class, () -> diplomaServiceImpl.deleteDiploma(1L, 1L));
        verify(diplomaRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }
}
