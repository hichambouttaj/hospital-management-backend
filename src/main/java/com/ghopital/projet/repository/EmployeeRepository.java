package com.ghopital.projet.repository;

import com.ghopital.projet.entity.Employee;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.HospitalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCin(String cin);
    Optional<Employee> findByRegistrationNumber(String registrationNumber);
    Page<Employee> findAllByHospitalServiceId(Long id, Pageable pageable);
    Page<Employee> findAllByCinIsLikeIgnoreCase(String cin, Pageable pageable);
    Page<Employee> findAllByFirstNameIsLikeIgnoreCase(String firstName, Pageable pageable);
    Page<Employee> findAllByLastNameIsIgnoreCase(String lastName, Pageable pageable);
    Page<Employee> findAllByRegistrationNumberIsLikeIgnoreCase(String registrationNumber, Pageable pageable);
    Page<Employee> findAllByRecruitmentDate(LocalDate recruitmentDate, Pageable pageable);
}