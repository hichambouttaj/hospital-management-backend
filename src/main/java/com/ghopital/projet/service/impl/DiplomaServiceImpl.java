package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.request.DiplomaDtoRequest;
import com.ghopital.projet.dto.response.DiplomaDtoResponse;
import com.ghopital.projet.entity.Diploma;
import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.DiplomaMapper;
import com.ghopital.projet.repository.DiplomaRepository;
import com.ghopital.projet.repository.EmployeeRepository;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.service.DiplomaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiplomaServiceImpl implements DiplomaService {
    private final DiplomaRepository diplomaRepository;
    private final FileRepository fileRepository;
    private final EmployeeRepository employeeRepository;

    public DiplomaServiceImpl(DiplomaRepository diplomaRepository, FileRepository fileRepository, EmployeeRepository employeeRepository) {
        this.diplomaRepository = diplomaRepository;
        this.fileRepository = fileRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public DiplomaDtoResponse createDiploma(Long employeeId, DiplomaDtoRequest diplomaDto) {
        // DTO to entity
        Diploma diploma = DiplomaMapper.INSTANCE.diplomaDtoRequestToDiploma(diplomaDto);

        // get employee from db
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        // add employee to diploma
        diploma.setEmployee(employee);

        // save to db
        Diploma savedDiploma = diplomaRepository.save(diploma);

        return DiplomaMapper.INSTANCE.diplomaToDiplomaDtoResponse(savedDiploma);
    }

    @Override
    public DiplomaDtoResponse getDiplomaById(Long employeeId, Long diplomaId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);
        return DiplomaMapper.INSTANCE.diplomaToDiplomaDtoResponse(diploma);
    }

    @Override
    public List<DiplomaDtoResponse> getAllDiploma() {
        return diplomaRepository.findAll().stream()
                .map(DiplomaMapper.INSTANCE::diplomaToDiplomaDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiplomaDtoResponse> getEmployeeDiplomas(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );
        return employee.getDiplomas().stream()
                .map(DiplomaMapper.INSTANCE::diplomaToDiplomaDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DiplomaDtoResponse updateDiploma(Long employeeId, Long diplomaId, DiplomaDtoRequest diplomaDto) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);

        if(StringUtils.hasText(diplomaDto.title())) {
            diploma.setTitle(diplomaDto.title());
        }

        if(diplomaDto.startDate() != null) {
            diploma.setStartDate(diplomaDto.startDate());
        }

        if (diplomaDto.endDate() != null) {
            diploma.setEndDate(diplomaDto.endDate());
        }

        Diploma updatedDiploma = diplomaRepository.save(diploma);

        return DiplomaMapper.INSTANCE.diplomaToDiplomaDtoResponse(updatedDiploma);
    }

    @Override
    @Transactional
    public DiplomaDtoResponse addDocumentToDiploma(Long employeeId, Long diplomaId, Long documentId) {
        checkDiplomaByEmployee(employeeId, diplomaId);

        Diploma diploma = diplomaRepository.findById(diplomaId).orElseThrow(
                () -> new ResourceNotFoundException("Diploma", "id", diplomaId.toString())
        );

        File document = fileRepository.findById(documentId).orElseThrow(
                () -> new ResourceNotFoundException("File", "id", documentId.toString())
        );

        diploma.setDocument(document);

        Diploma updatedDiploma = diplomaRepository.save(diploma);

        return DiplomaMapper.INSTANCE.diplomaToDiplomaDtoResponse(updatedDiploma);
    }
    @Override
    public void deleteDiploma(Long employeeId, Long diplomaId) {
        Diploma diploma = checkDiplomaByEmployee(employeeId, diplomaId);
        diplomaRepository.delete(diploma);
    }

    private Diploma checkDiplomaByEmployee(Long employeeId, Long diplomaId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId.toString())
        );

        Diploma diploma = diplomaRepository.findById(diplomaId).orElseThrow(
                () -> new ResourceNotFoundException("Diploma", "id", diplomaId.toString())
        );

        if(!diploma.getEmployee().getId().equals(employee.getId())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Diploma does not belong to employee");
        }
        return diploma;
    }
}
