package com.ghopital.projet.repository;

import com.ghopital.projet.entity.HospitalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<HospitalService, Long> {
    Optional<HospitalService> findByName(String serviceName);
}