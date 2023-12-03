package com.ghopital.projet.repository;

import com.ghopital.projet.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}