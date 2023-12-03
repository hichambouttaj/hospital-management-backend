package com.ghopital.projet.mapper;

import com.ghopital.projet.dto.request.EmployeeDtoRequest;
import com.ghopital.projet.dto.response.EmployeeDtoResponse;
import com.ghopital.projet.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
    Employee employeeDtoRequestToEmployee(EmployeeDtoRequest employeeDtoRequest);
    @Mapping(target = "serviceName", source = "employee.service.name")
    EmployeeDtoResponse employeeToEmployeeDtoResponse(Employee employee);
}
