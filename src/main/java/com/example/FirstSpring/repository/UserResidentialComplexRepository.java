package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.ResidentialComplex;
import com.example.FirstSpring.models.User;
import com.example.FirstSpring.models.UserResidentialcomplex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserResidentialComplexRepository extends JpaRepository<UserResidentialcomplex, Long> {
    List<UserResidentialcomplex> findByUser(User user);
    List<UserResidentialcomplex> findByResidentialComplex(ResidentialComplex residentialComplex);

    Optional<UserResidentialcomplex> findById(Long id);
    UserResidentialcomplex findByUserAndResidentialComplex(User user, ResidentialComplex residentialComplex);
}
