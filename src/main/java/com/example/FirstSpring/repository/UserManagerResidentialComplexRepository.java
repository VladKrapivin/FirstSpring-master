package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.ResidentialComplex;
import com.example.FirstSpring.models.User;
import com.example.FirstSpring.models.UserManagerResidentialComplex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserManagerResidentialComplexRepository extends JpaRepository<UserManagerResidentialComplex, Long> {
    UserManagerResidentialComplex findByUserAndCoin(User user, ResidentialComplex residentialComplex);

    List<UserManagerResidentialComplex> findByUser(User user);
    List<UserManagerResidentialComplex> findByResidentialComplex(ResidentialComplex residentialComplex);
    List<UserManagerResidentialComplex> findByManager(User broker);
}
