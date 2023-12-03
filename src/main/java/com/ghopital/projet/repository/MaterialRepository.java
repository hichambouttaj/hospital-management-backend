package com.ghopital.projet.repository;

import com.ghopital.projet.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}