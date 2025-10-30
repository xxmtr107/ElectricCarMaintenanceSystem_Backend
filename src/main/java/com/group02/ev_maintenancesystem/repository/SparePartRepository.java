package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Long> {
    boolean existsByPartNumber(String partNumber);
    Optional<SparePart> findByNameIgnoreCaseAndIdNot(String name, Long id);
}
