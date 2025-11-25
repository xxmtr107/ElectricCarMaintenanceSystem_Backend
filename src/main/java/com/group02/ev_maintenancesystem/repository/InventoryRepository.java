package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Tìm kho theo trung tâm và mã phụ tùng
    Optional<Inventory> findByServiceCenterIdAndSparePartId(Long centerId, Long sparePartId);

    // Lấy danh sách kho của 1 trung tâm
    List<Inventory> findByServiceCenterId(Long centerId);
}