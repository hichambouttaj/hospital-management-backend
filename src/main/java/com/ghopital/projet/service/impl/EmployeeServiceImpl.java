package com.ghopital.projet.service.impl;

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
import com.ghopital.projet.mapper.EmployeeMapper;
import com.ghopital.projet.mapper.FileMapper;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.repository.ServiceRepository;
import com.ghopital.projet.service.EmployeeService;
import com.ghopital.projet.util.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final FileRepository fileRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ServiceRepository serviceRepository, FileRepository fileRepository) {
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public EmployeeDtoResponse createEmployee(EmployeeDtoRequest employeeDto) {
        // DTO to entity
        Employee employee = EmployeeMapper.INSTANCE.employeeDtoRequestToEmployee(employeeDto);

        // get service from db
        HospitalService hospitalService = serviceRepository.findByName(employeeDto.service().name()).orElseThrow(
                () -> new ResourceNotFoundException("Service", "name", employeeDto.service().name())
        );

        // add service to employee
        employee.setService(hospitalService);

        // save in db
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(savedEmployee);
    }

    @Override
    public EmployeeDtoResponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );
        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(employee);
    }

    @Override
    public EmployeeDtoResponse getEmployeeByCin(String cin) {
        Employee employee = employeeRepository.findByCin(cin).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "cin", cin)
        );
        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(employee);
    }

    @Override
    public EmployeeDtoResponse getEmployeeByRN(String registrationNumber) {
        Employee employee = employeeRepository.findByRegistrationNumber(registrationNumber).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "registration number", registrationNumber)
        );
        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(employee);
    }

    @Override
    public List<EmployeeDtoResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeDtoResponse).collect(Collectors.toList());
    }

    @Override
    public PageDtoResponse getAllEmployees(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        PageRequest pageRequest = SortUtil.buildPageRequest(pageNumber, pageSize, sortField, sortDirection);

        Page<Employee> employeePage = employeeRepository.findAll(pageRequest);

        List<EmployeeDtoResponse> employeeDtoResponseList = employeePage.getContent().stream()
                .map(EmployeeMapper.INSTANCE::employeeToEmployeeDtoResponse)
                .collect(Collectors.toList());

        return SortUtil.generatePageDtoResponse(
                employeeDtoResponseList,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast());
    }
    @Override
    public List<FileDtoResponse> getDocumentsOfEmployee(Long employeeId) {
        // Get employee from database
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        Set<File> documents = employee.getDocuments();

        return documents.stream()
                .map(document -> new FileDtoResponse(
                        document.getId(),
                        document.getName(),
                        document.getType(),
                        document.getData().length))
                .collect(Collectors.toList());
    }

    @Override
    public PageDtoResponse getEmployeesByService(Long serviceId, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        HospitalService hospitalService = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service", "id", serviceId.toString())
        );

        PageRequest pageRequest = SortUtil.buildPageRequest(pageNumber, pageSize, sortField, sortDirection);

        Page<Employee> employeePage = employeeRepository.findAllByHospitalServiceId(hospitalService.getId(), pageRequest);

        List<EmployeeDtoResponse> employeeDtoResponseList = employeePage.getContent().stream()
                .map(EmployeeMapper.INSTANCE::employeeToEmployeeDtoResponse)
                .collect(Collectors.toList());

        return SortUtil.generatePageDtoResponse(
                employeeDtoResponseList,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast());
    }

    @Override
    @Transactional
    public EmployeeDtoResponse updateEmployee(EmployeeDtoRequest employeeDto, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        if(StringUtils.hasText(employeeDto.firstName())) {
            employee.setFirstName(employeeDto.firstName());
        }

        if(StringUtils.hasText(employeeDto.lastName())) {
            employee.setLastName(employeeDto.lastName());
        }

        if(StringUtils.hasText(employeeDto.cin())) {
            employee.setCin(employeeDto.cin());
        }

        if(StringUtils.hasText(employeeDto.registrationNumber())) {
            employee.setRegistrationNumber(employeeDto.registrationNumber());
        }

        if(employeeDto.recruitmentDate() != null) {
            employee.setRecruitmentDate(employeeDto.recruitmentDate());
        }

        if(employeeDto.service() != null && StringUtils.hasText(employeeDto.service().name())) {
            HospitalService hospitalService = serviceRepository.findByName(employeeDto.service().name()).orElseThrow(
                    () -> new ResourceNotFoundException("Service", "name", employeeDto.service().name())
            );
            employee.setService(hospitalService);
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDtoResponse addDocumentToEmployee(Long employeeId, Long documentId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        File document = fileRepository.findById(documentId).orElseThrow(
                () -> new ResourceNotFoundException("File", "id", documentId.toString())
        );

        if(employee.getDocuments() == null) {
            employee.setDocuments(new HashSet<>());
        }

        employee.getDocuments().add(document);

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(updatedEmployee);
    }

    @Override
    public void deleteDocumentOfEmployee(Long employeeId, Long documentId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        File document = fileRepository.findById(documentId).orElseThrow(
                () -> new ResourceNotFoundException("File", "id", documentId.toString())
        );

        // check if document of employee
        checkIfDocumentOfEmployee(employee, documentId);

        // remove document from employee documents
        Set<File> documents = employee.getDocuments();

        Iterator<File> iterator = documents.iterator();
        while(iterator.hasNext()) {
            File file = iterator.next();
            if(file.getId().equals(documentId)) {
                iterator.remove();
                break;
            }
        }

        employee.setDocuments(documents);

        // save employee update
        employeeRepository.save(employee);

        // remove document from database
        fileRepository.delete(document);
    }

    private void checkIfDocumentOfEmployee(Employee employee, Long documentId) {
        // get list of document of employee
        Set<File> documents = employee.getDocuments();
        for(File file : documents) {
            if(file.getId().equals(documentId)) {
                return;
            }
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "Document does not belong to employee");
    }

    @Override
    @Transactional
    public EmployeeDtoResponse addServiceToEmployee(Long employeeId, ServiceDtoRequest serviceDtoRequest) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        HospitalService hospitalService = serviceRepository.findByName(serviceDtoRequest.name()).orElseThrow(
                () -> new ResourceNotFoundException("Service", "name", serviceDtoRequest.name())
        );

        employee.setService(hospitalService);
        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.INSTANCE.employeeToEmployeeDtoResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );
        employeeRepository.delete(employee);
    }
}
