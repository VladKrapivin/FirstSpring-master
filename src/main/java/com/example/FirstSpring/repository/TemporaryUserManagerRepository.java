package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.ResidentialComplex;
import com.example.FirstSpring.models.TemporaryUserManager;
import com.example.FirstSpring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemporaryUserManagerRepository extends JpaRepository<TemporaryUserManager, Long> {

    TemporaryUserManager findByUserAndCoin(User user, ResidentialComplex residentialComplex);

    List<TemporaryUserManager> findByUser(User user);
    List<TemporaryUserManager> findByCoin(ResidentialComplex residentialComplex);
}
