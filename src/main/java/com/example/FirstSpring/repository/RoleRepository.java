package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
