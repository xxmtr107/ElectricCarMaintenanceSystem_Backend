package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
