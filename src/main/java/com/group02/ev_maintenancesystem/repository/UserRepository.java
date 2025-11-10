package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findAllByRole(Role role);
    Optional<User> findByIdAndRole(Long id, Role role);

    Collection<Object> findAllByRoleAndServiceCenterId(Role role, Long centerId);
}