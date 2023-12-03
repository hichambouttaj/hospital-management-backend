package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.EmployeeDtoRequest;
import com.ghopital.projet.dto.request.ServiceDtoRequest;
import com.ghopital.projet.dto.response.EmployeeDtoResponse;
import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.dto.response.PageDtoResponse;
import com.ghopital.projet.service.EmployeeService;
import com.ghopital.projet.util.Constants;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    public ResponseEntity<EmployeeDtoResponse> createEmployee(@Valid @RequestBody EmployeeDtoRequest employeeDto) {
        EmployeeDtoResponse response = employeeService.createEmployee(employeeDto);
        return ResponseEntity.created(URI.create("/employees/" + response.cin()))
                .body(response);
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDtoResponse> getEmployeeById(@PathVariable(name = "employeeId") Long employeeId) {
        EmployeeDtoResponse response = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<EmployeeDtoResponse>> getAllEmployees() {
        List<EmployeeDtoResponse> responses = employeeService.getAllEmployees();
        return ResponseEntity.ok(responses);
    }
//    @GetMapping
//    public ResponseEntity<PageDtoResponse> getAllEmployees(
//            @RequestParam(name = "pageNumber") Integer pageNumber,
//            @RequestParam(name = "pageSize") Integer pageSize,
//            @RequestParam(name = "sortField") String sortField,
//            @RequestParam(name = "sortDirection") String sortDirection) {
//        return ResponseEntity.ok(employeeService.getAllEmployees(pageNumber, pageSize, sortField, sortDirection));
//    }
//    @GetMapping("/service/{id}")
//    public ResponseEntity<PageDtoResponse> getAllEmployeeByService(
//            @PathVariable(name = "id") Long serviceId,
//            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
//            @RequestParam(name = "pageSize", required = false) Integer pageSize,
//            @RequestParam(name = "sortField", required = false) String sortField,
//            @RequestParam(name = "sortDirection", required = false) String sortDirection) {
//        return ResponseEntity.ok(employeeService.getEmployeesByService(serviceId, pageNumber, pageSize, sortField, sortDirection));
//    }
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDtoResponse> updateEmployee(
            @PathVariable(name = "employeeId") Long employeeId, @Valid @RequestBody EmployeeDtoRequest employeeDto) {
        EmployeeDtoResponse response = employeeService.updateEmployee(employeeDto, employeeId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{employeeId}/documents")
    public ResponseEntity<List<FileDtoResponse>> getDocumentsOfEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        List<FileDtoResponse> responses = employeeService.getDocumentsOfEmployee(employeeId);
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{employeeId}/documents/{documentId}")
    public ResponseEntity<EmployeeDtoResponse> addDocumentToEmployee(
            @PathVariable(name = "employeeId") Long employeeId, @PathVariable(name = "documentId") Long documentId) {
        EmployeeDtoResponse response = employeeService.addDocumentToEmployee(employeeId, documentId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{employeeId}/documents/{documentId}")
    public ResponseEntity<String> deleteDocumentOfEmployee(
            @PathVariable(name = "employeeId") Long employeeId, @PathVariable(name = "documentId") Long documentId) {
        employeeService.deleteDocumentOfEmployee(employeeId, documentId);
        return ResponseEntity.ok("Document deleted successfully");
    }
    @PutMapping("/{employeeId}/services")
    public ResponseEntity<EmployeeDtoResponse> addServiceToEmployee(
            @PathVariable(name = "employeeId") Long employeeId,
            @Valid @RequestBody ServiceDtoRequest serviceDtoRequest) {
        EmployeeDtoResponse response = employeeService.addServiceToEmployee(employeeId, serviceDtoRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}
