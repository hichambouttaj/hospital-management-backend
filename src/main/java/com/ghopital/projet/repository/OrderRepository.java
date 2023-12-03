package com.ghopital.projet.repository;

import com.ghopital.projet.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByCode(Long code);
}