package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.EmployeeDtoRequest;
import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.EmployeeDtoResponse;
import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.dto.response.PageDtoResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeDtoResponse createEmployee(EmployeeDtoRequest employeeDto);
    EmployeeDtoResponse getEmployeeById(Long employeeId);
    EmployeeDtoResponse getEmployeeByCin(String cin);
    EmployeeDtoResponse getEmployeeByRN(String registrationNumber);
    List<EmployeeDtoResponse> getAllEmployees();
    PageDtoResponse getAllEmployees(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
    List<FileDtoResponse> getDocumentsOfEmployee(Long employeeId);
    PageDtoResponse getEmployeesByService(Long serviceId, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
    EmployeeDtoResponse updateEmployee(EmployeeDtoRequest employeeDto, Long employeeId);
    EmployeeDtoResponse addDocumentToEmployee(Long employeeId, Long documentId);
    EmployeeDtoResponse addServiceToEmployee(Long employeeId, ServiceDtoRequest serviceDtoRequest);
    void deleteEmployee(Long employeeId);
    void deleteDocumentOfEmployee(Long employeeId, Long documentId);
}
