package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.ResidentialComplex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<ResidentialComplex, Long> {
    Optional<ResidentialComplex> findById(Long ID);
}
