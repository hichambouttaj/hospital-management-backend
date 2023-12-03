package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.request.EmployeeDtoRequest;
import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.EmployeeDtoResponse;
import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.dto.response.PageDtoResponse;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.repository.ServiceRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmployeeServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class EmployeeServiceImplTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private ServiceRepository serviceRepository;

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#createEmployee(EmployeeDtoRequest)}
     */
    @Test
    void testCreateEmployee() {
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
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee);

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualCreateEmployeeResult = employeeServiceImpl.createEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualCreateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualCreateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualCreateEmployeeResult.cin());
        assertEquals("Doe", actualCreateEmployeeResult.lastName());
        assertEquals("Jane", actualCreateEmployeeResult.firstName());
        assertEquals("Name", actualCreateEmployeeResult.serviceName());
        assertEquals(1L, actualCreateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#createEmployee(EmployeeDtoRequest)}
     */
    @Test
    void testCreateEmployee2() {
        when(employeeRepository.save(Mockito.<Employee>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        assertThrows(AppException.class, () -> employeeServiceImpl.createEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name"))));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#createEmployee(EmployeeDtoRequest)}
     */
    @Test
    void testCreateEmployee3() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.createEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name"))));
        verify(serviceRepository).findByName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById() {
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
        EmployeeDtoResponse actualEmployeeById = employeeServiceImpl.getEmployeeById(1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        assertEquals("1970-01-01", actualEmployeeById.recruitmentDate().toString());
        assertEquals("42", actualEmployeeById.registrationNumber());
        assertEquals("Cin", actualEmployeeById.cin());
        assertEquals("Doe", actualEmployeeById.lastName());
        assertEquals("Jane", actualEmployeeById.firstName());
        assertEquals("Name", actualEmployeeById.serviceName());
        assertEquals(1L, actualEmployeeById.id().longValue());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById2() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getEmployeeById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById3() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.getEmployeeById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getEmployeeByCin(String)}
     */
    @Test
    void testGetEmployeeByCin() {
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
        when(employeeRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult);
        EmployeeDtoResponse actualEmployeeByCin = employeeServiceImpl.getEmployeeByCin("Cin");
        verify(employeeRepository).findByCin(Mockito.<String>any());
        assertEquals("1970-01-01", actualEmployeeByCin.recruitmentDate().toString());
        assertEquals("42", actualEmployeeByCin.registrationNumber());
        assertEquals("Cin", actualEmployeeByCin.cin());
        assertEquals("Doe", actualEmployeeByCin.lastName());
        assertEquals("Jane", actualEmployeeByCin.firstName());
        assertEquals("Name", actualEmployeeByCin.serviceName());
        assertEquals(1L, actualEmployeeByCin.id().longValue());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getEmployeeByCin(String)}
     */
    @Test
    void testGetEmployeeByCin2() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findByCin(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getEmployeeByCin("Cin"));
        verify(employeeRepository).findByCin(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getEmployeeByCin(String)}
     */
    @Test
    void testGetEmployeeByCin3() {
        when(employeeRepository.findByCin(Mockito.<String>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.getEmployeeByCin("Cin"));
        verify(employeeRepository).findByCin(Mockito.<String>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getEmployeeByRN(String)}
     */
    @Test
    void testGetEmployeeByRN() {
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
        when(employeeRepository.findByRegistrationNumber(Mockito.<String>any())).thenReturn(ofResult);
        EmployeeDtoResponse actualEmployeeByRN = employeeServiceImpl.getEmployeeByRN("42");
        verify(employeeRepository).findByRegistrationNumber(Mockito.<String>any());
        assertEquals("1970-01-01", actualEmployeeByRN.recruitmentDate().toString());
        assertEquals("42", actualEmployeeByRN.registrationNumber());
        assertEquals("Cin", actualEmployeeByRN.cin());
        assertEquals("Doe", actualEmployeeByRN.lastName());
        assertEquals("Jane", actualEmployeeByRN.firstName());
        assertEquals("Name", actualEmployeeByRN.serviceName());
        assertEquals(1L, actualEmployeeByRN.id().longValue());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getEmployeeByRN(String)}
     */
    @Test
    void testGetEmployeeByRN2() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findByRegistrationNumber(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getEmployeeByRN("42"));
        verify(employeeRepository).findByRegistrationNumber(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getEmployeeByRN(String)}
     */
    @Test
    void testGetEmployeeByRN3() {
        when(employeeRepository.findByRegistrationNumber(Mockito.<String>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.getEmployeeByRN("42"));
        verify(employeeRepository).findByRegistrationNumber(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        List<EmployeeDtoResponse> actualAllEmployees = employeeServiceImpl.getAllEmployees();
        verify(employeeRepository).findAll();
        assertTrue(actualAllEmployees.isEmpty());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees2() {
        when(employeeRepository.findAll()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.getAllEmployees());
        verify(employeeRepository).findAll();
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getAllEmployees(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllEmployees3() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        PageDtoResponse actualAllEmployees = employeeServiceImpl.getAllEmployees(10, 3, "Sort Field", "Sort Direction");
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
        assertEquals(0, actualAllEmployees.pageNumber());
        assertEquals(0, actualAllEmployees.pageSize());
        assertEquals(0L, actualAllEmployees.totalElements());
        assertEquals(1, actualAllEmployees.totalPages());
        assertTrue(actualAllEmployees.last());
        assertEquals(content, actualAllEmployees.contents());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getAllEmployees(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllEmployees4() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        PageDtoResponse actualAllEmployees = employeeServiceImpl.getAllEmployees(0, 3, "Sort Field", "Sort Direction");
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
        assertEquals(0, actualAllEmployees.pageNumber());
        assertEquals(0, actualAllEmployees.pageSize());
        assertEquals(0L, actualAllEmployees.totalElements());
        assertEquals(1, actualAllEmployees.totalPages());
        assertTrue(actualAllEmployees.last());
        assertEquals(content, actualAllEmployees.contents());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getAllEmployees(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllEmployees5() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        PageDtoResponse actualAllEmployees = employeeServiceImpl.getAllEmployees(10, 3, "", "Sort Direction");
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
        assertEquals(0, actualAllEmployees.pageNumber());
        assertEquals(0, actualAllEmployees.pageSize());
        assertEquals(0L, actualAllEmployees.totalElements());
        assertEquals(1, actualAllEmployees.totalPages());
        assertTrue(actualAllEmployees.last());
        assertEquals(content, actualAllEmployees.contents());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getAllEmployees(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllEmployees6() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        PageDtoResponse actualAllEmployees = employeeServiceImpl.getAllEmployees(10, 3, "Sort Field", "");
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
        assertEquals(0, actualAllEmployees.pageNumber());
        assertEquals(0, actualAllEmployees.pageSize());
        assertEquals(0L, actualAllEmployees.totalElements());
        assertEquals(1, actualAllEmployees.totalPages());
        assertTrue(actualAllEmployees.last());
        assertEquals(content, actualAllEmployees.contents());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getAllEmployees(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllEmployees7() {
        when(employeeRepository.findAll(Mockito.<Pageable>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.getAllEmployees(10, 3, "Sort Field", "Sort Direction"));
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getDocumentsOfEmployee(Long)}
     */
    @Test
    void testGetDocumentsOfEmployee() {
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
        List<FileDtoResponse> actualDocumentsOfEmployee = employeeServiceImpl.getDocumentsOfEmployee(1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        assertTrue(actualDocumentsOfEmployee.isEmpty());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getDocumentsOfEmployee(Long)}
     */
    @Test
    void testGetDocumentsOfEmployee2() throws UnsupportedEncodingException {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");

        HashSet<File> documents = new HashSet<>();
        documents.add(file);

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        Employee employee = new Employee();
        employee.setCin("Cin");
        employee.setDiplomas(new HashSet<>());
        employee.setDocuments(documents);
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        employee.setRecruitmentDate(LocalDate.of(1970, 1, 1));
        employee.setRegistrationNumber("42");
        employee.setService(hospitalService);
        employee.setVacations(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        List<FileDtoResponse> actualDocumentsOfEmployee = employeeServiceImpl.getDocumentsOfEmployee(1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        FileDtoResponse getResult = actualDocumentsOfEmployee.get(0);
        assertEquals("Name", getResult.name());
        assertEquals("Type", getResult.type());
        assertEquals(1, actualDocumentsOfEmployee.size());
        assertEquals(1L, getResult.id().longValue());
        assertEquals(8L, getResult.size());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getDocumentsOfEmployee(Long)}
     */
    @Test
    void testGetDocumentsOfEmployee3() throws UnsupportedEncodingException {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");

        File file2 = new File();
        file2.setData("AXAXAXAX".getBytes("UTF-8"));
        file2.setId(2L);
        file2.setName("42");
        file2.setType("42");

        HashSet<File> documents = new HashSet<>();
        documents.add(file2);
        documents.add(file);

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        Employee employee = new Employee();
        employee.setCin("Cin");
        employee.setDiplomas(new HashSet<>());
        employee.setDocuments(documents);
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        employee.setRecruitmentDate(LocalDate.of(1970, 1, 1));
        employee.setRegistrationNumber("42");
        employee.setService(hospitalService);
        employee.setVacations(new HashSet<>());
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        List<FileDtoResponse> actualDocumentsOfEmployee = employeeServiceImpl.getDocumentsOfEmployee(1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        assertEquals(2, actualDocumentsOfEmployee.size());
        assertEquals(8L, actualDocumentsOfEmployee.get(0).size());
        assertEquals(8L, actualDocumentsOfEmployee.get(1).size());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#getDocumentsOfEmployee(Long)}
     */
    @Test
    void testGetDocumentsOfEmployee4() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getDocumentsOfEmployee(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getDocumentsOfEmployee(Long)}
     */
    @Test
    void testGetDocumentsOfEmployee5() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.getDocumentsOfEmployee(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getEmployeesByService(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetEmployeesByService() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(content));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        PageDtoResponse actualEmployeesByService = employeeServiceImpl.getEmployeesByService(1L, 10, 3, "Sort Field",
                "Sort Direction");
        verify(employeeRepository).findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
        assertEquals(0, actualEmployeesByService.pageNumber());
        assertEquals(0, actualEmployeesByService.pageSize());
        assertEquals(0L, actualEmployeesByService.totalElements());
        assertEquals(1, actualEmployeesByService.totalPages());
        assertTrue(actualEmployeesByService.last());
        assertEquals(content, actualEmployeesByService.contents());
    }

    /**
     * Method under test:  {@link EmployeeServiceImpl#getEmployeesByService(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetEmployeesByService2() {
        when(employeeRepository.findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class,
                () -> employeeServiceImpl.getEmployeesByService(1L, 10, 3, "Sort Field", "Sort Direction"));
        verify(employeeRepository).findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getEmployeesByService(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetEmployeesByService3() {
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.getEmployeesByService(1L, 10, 3, "Sort Field", "Sort Direction"));
        verify(serviceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getEmployeesByService(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetEmployeesByService4() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(content));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        PageDtoResponse actualEmployeesByService = employeeServiceImpl.getEmployeesByService(1L, 0, 3, "Sort Field",
                "Sort Direction");
        verify(employeeRepository).findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
        assertEquals(0, actualEmployeesByService.pageNumber());
        assertEquals(0, actualEmployeesByService.pageSize());
        assertEquals(0L, actualEmployeesByService.totalElements());
        assertEquals(1, actualEmployeesByService.totalPages());
        assertTrue(actualEmployeesByService.last());
        assertEquals(content, actualEmployeesByService.contents());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getEmployeesByService(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetEmployeesByService5() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(content));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        PageDtoResponse actualEmployeesByService = employeeServiceImpl.getEmployeesByService(1L, 10, 3, "",
                "Sort Direction");
        verify(employeeRepository).findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
        assertEquals(0, actualEmployeesByService.pageNumber());
        assertEquals(0, actualEmployeesByService.pageSize());
        assertEquals(0L, actualEmployeesByService.totalElements());
        assertEquals(1, actualEmployeesByService.totalPages());
        assertTrue(actualEmployeesByService.last());
        assertEquals(content, actualEmployeesByService.contents());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#getEmployeesByService(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetEmployeesByService6() {
        ArrayList<Employee> content = new ArrayList<>();
        when(employeeRepository.findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(content));

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult = Optional.of(hospitalService);
        when(serviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        PageDtoResponse actualEmployeesByService = employeeServiceImpl.getEmployeesByService(1L, 10, 3, "Sort Field", "");
        verify(employeeRepository).findAllByHospitalServiceId(Mockito.<Long>any(), Mockito.<Pageable>any());
        verify(serviceRepository).findById(Mockito.<Long>any());
        assertEquals(0, actualEmployeesByService.pageNumber());
        assertEquals(0, actualEmployeesByService.pageSize());
        assertEquals(0L, actualEmployeesByService.totalElements());
        assertEquals(1, actualEmployeesByService.totalPages());
        assertTrue(actualEmployeesByService.last());
        assertEquals(content, actualEmployeesByService.contents());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L);
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee2() {
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

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        assertThrows(AppException.class, () -> employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee4() {
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
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee5() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest(null, "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L);
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee6() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", null, "Cin", "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L);
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee7() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", null, "42", recruitmentDate, new ServiceDtoRequest("Name")), 1L);
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee8() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", null, recruitmentDate, new ServiceDtoRequest("Name")), 1L);
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee9() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl
                .updateEmployee(new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", null, new ServiceDtoRequest("Name")), 1L);
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee10() {
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
        LocalDate recruitmentDate = LocalDate.of(1970, 1, 1);
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl.updateEmployee(
                new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", recruitmentDate, new ServiceDtoRequest(null)), 1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#updateEmployee(EmployeeDtoRequest, Long)}
     */
    @Test
    void testUpdateEmployee11() {
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
        EmployeeDtoResponse actualUpdateEmployeeResult = employeeServiceImpl
                .updateEmployee(new EmployeeDtoRequest("Jane", "Doe", "Cin", "42", LocalDate.of(1970, 1, 1), null), 1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualUpdateEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualUpdateEmployeeResult.registrationNumber());
        assertEquals("Cin", actualUpdateEmployeeResult.cin());
        assertEquals("Doe", actualUpdateEmployeeResult.lastName());
        assertEquals("Jane", actualUpdateEmployeeResult.firstName());
        assertEquals("Name", actualUpdateEmployeeResult.serviceName());
        assertEquals(1L, actualUpdateEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addDocumentToEmployee(Long, Long)}
     */
    @Test
    void testAddDocumentToEmployee() throws UnsupportedEncodingException {
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

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult2 = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        EmployeeDtoResponse actualAddDocumentToEmployeeResult = employeeServiceImpl.addDocumentToEmployee(1L, 1L);
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualAddDocumentToEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualAddDocumentToEmployeeResult.registrationNumber());
        assertEquals("Cin", actualAddDocumentToEmployeeResult.cin());
        assertEquals("Doe", actualAddDocumentToEmployeeResult.lastName());
        assertEquals("Jane", actualAddDocumentToEmployeeResult.firstName());
        assertEquals("Name", actualAddDocumentToEmployeeResult.serviceName());
        assertEquals(1L, actualAddDocumentToEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addDocumentToEmployee(Long, Long)}
     */
    @Test
    void testAddDocumentToEmployee2() throws UnsupportedEncodingException {
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

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult2 = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> employeeServiceImpl.addDocumentToEmployee(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addDocumentToEmployee(Long, Long)}
     */
    @Test
    void testAddDocumentToEmployee3() throws UnsupportedEncodingException {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.addDocumentToEmployee(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addDocumentToEmployee(Long, Long)}
     */
    @Test
    void testAddDocumentToEmployee4() {
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
        Optional<File> emptyResult = Optional.empty();
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.addDocumentToEmployee(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee() throws UnsupportedEncodingException {
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

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult2 = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> employeeServiceImpl.deleteDocumentOfEmployee(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee2() {
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
        when(fileRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> employeeServiceImpl.deleteDocumentOfEmployee(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee3() throws UnsupportedEncodingException {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Document does not belong to employee");
        file.setType("Document does not belong to employee");

        HashSet<File> documents = new HashSet<>();
        documents.add(file);

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        Employee employee = new Employee();
        employee.setCin("Cin");
        employee.setDiplomas(new HashSet<>());
        employee.setDocuments(documents);
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

        File file2 = new File();
        file2.setData("AXAXAXAX".getBytes("UTF-8"));
        file2.setId(1L);
        file2.setName("Name");
        file2.setType("Type");
        Optional<File> ofResult2 = Optional.of(file2);
        doNothing().when(fileRepository).delete(Mockito.<File>any());
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        employeeServiceImpl.deleteDocumentOfEmployee(1L, 1L);
        verify(fileRepository).delete(Mockito.<File>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee4() throws UnsupportedEncodingException {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Document does not belong to employee");
        file.setType("Document does not belong to employee");

        HashSet<File> documents = new HashSet<>();
        documents.add(file);

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        Employee employee = new Employee();
        employee.setCin("Cin");
        employee.setDiplomas(new HashSet<>());
        employee.setDocuments(documents);
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

        File file2 = new File();
        file2.setData("AXAXAXAX".getBytes("UTF-8"));
        file2.setId(1L);
        file2.setName("Name");
        file2.setType("Type");
        Optional<File> ofResult2 = Optional.of(file2);
        doThrow(new AppException(HttpStatus.CONTINUE, "An error occurred")).when(fileRepository)
                .delete(Mockito.<File>any());
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> employeeServiceImpl.deleteDocumentOfEmployee(1L, 1L));
        verify(fileRepository).delete(Mockito.<File>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee5() throws UnsupportedEncodingException {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Document does not belong to employee");
        file.setType("Document does not belong to employee");

        File file2 = new File();
        file2.setData(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        file2.setId(2L);
        file2.setName("42");
        file2.setType("42");

        HashSet<File> documents = new HashSet<>();
        documents.add(file2);
        documents.add(file);

        HospitalService hospitalService = new HospitalService();
        hospitalService.setId(1L);
        hospitalService.setName("Name");
        hospitalService.setProductOrders(new HashSet<>());

        Employee employee = new Employee();
        employee.setCin("Cin");
        employee.setDiplomas(new HashSet<>());
        employee.setDocuments(documents);
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

        File file3 = new File();
        file3.setData("AXAXAXAX".getBytes("UTF-8"));
        file3.setId(1L);
        file3.setName("Name");
        file3.setType("Type");
        Optional<File> ofResult2 = Optional.of(file3);
        doNothing().when(fileRepository).delete(Mockito.<File>any());
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        employeeServiceImpl.deleteDocumentOfEmployee(1L, 1L);
        verify(fileRepository).delete(Mockito.<File>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(fileRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#deleteDocumentOfEmployee(Long, Long)}
     */
    @Test
    void testDeleteDocumentOfEmployee6() throws UnsupportedEncodingException {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        Optional<File> ofResult = Optional.of(file);
        when(fileRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.deleteDocumentOfEmployee(1L, 1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addServiceToEmployee(Long, ServiceDtoRequest)}
     */
    @Test
    void testAddServiceToEmployee() {
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

        HospitalService hospitalService3 = new HospitalService();
        hospitalService3.setId(1L);
        hospitalService3.setName("Name");
        hospitalService3.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService3);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        EmployeeDtoResponse actualAddServiceToEmployeeResult = employeeServiceImpl.addServiceToEmployee(1L,
                new ServiceDtoRequest("Name"));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
        assertEquals("1970-01-01", actualAddServiceToEmployeeResult.recruitmentDate().toString());
        assertEquals("42", actualAddServiceToEmployeeResult.registrationNumber());
        assertEquals("Cin", actualAddServiceToEmployeeResult.cin());
        assertEquals("Doe", actualAddServiceToEmployeeResult.lastName());
        assertEquals("Jane", actualAddServiceToEmployeeResult.firstName());
        assertEquals("Name", actualAddServiceToEmployeeResult.serviceName());
        assertEquals(1L, actualAddServiceToEmployeeResult.id().longValue());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addServiceToEmployee(Long, ServiceDtoRequest)}
     */
    @Test
    void testAddServiceToEmployee2() {
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

        HospitalService hospitalService2 = new HospitalService();
        hospitalService2.setId(1L);
        hospitalService2.setName("Name");
        hospitalService2.setProductOrders(new HashSet<>());
        Optional<HospitalService> ofResult2 = Optional.of(hospitalService2);
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> employeeServiceImpl.addServiceToEmployee(1L, new ServiceDtoRequest("Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).save(Mockito.<Employee>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addServiceToEmployee(Long, ServiceDtoRequest)}
     */
    @Test
    void testAddServiceToEmployee3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.addServiceToEmployee(1L, new ServiceDtoRequest("Name")));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link EmployeeServiceImpl#addServiceToEmployee(Long, ServiceDtoRequest)}
     */
    @Test
    void testAddServiceToEmployee4() {
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
        Optional<HospitalService> emptyResult = Optional.empty();
        when(serviceRepository.findByName(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class,
                () -> employeeServiceImpl.addServiceToEmployee(1L, new ServiceDtoRequest("Name")));
        verify(serviceRepository).findByName(Mockito.<String>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee() {
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
        doNothing().when(employeeRepository).delete(Mockito.<Employee>any());
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        employeeServiceImpl.deleteEmployee(1L);
        verify(employeeRepository).delete(Mockito.<Employee>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee2() {
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
        doThrow(new AppException(HttpStatus.CONTINUE, "An error occurred")).when(employeeRepository)
                .delete(Mockito.<Employee>any());
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class, () -> employeeServiceImpl.deleteEmployee(1L));
        verify(employeeRepository).delete(Mockito.<Employee>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeServiceImpl#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee3() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.deleteEmployee(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }
}
