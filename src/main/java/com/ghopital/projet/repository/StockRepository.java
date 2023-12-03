package com.ghopital.projet.repository;

import com.ghopital.projet.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}