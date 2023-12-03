package com.ghopital.projet.repository;

import com.ghopital.projet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCin(String cin);
    Boolean existsByEmail(String email);
    Boolean existsByCin(String cin);
}