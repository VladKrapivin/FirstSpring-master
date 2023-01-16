package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findById(Long ID);
}
