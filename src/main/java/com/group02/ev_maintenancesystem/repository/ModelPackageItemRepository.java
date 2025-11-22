package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import com.group02.ev_maintenancesystem.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelPackageItemRepository extends JpaRepository<ModelPackageItem, Long> {

    List<ModelPackageItem> findByVehicleModelIdOrderByCreatedAtDesc(Long vehicleModelId);

    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmGreaterThanOrderByCreatedAtDesc(Long vehicleModelId, Integer minKm);

    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(Long modelId, Integer milestoneKm);

    Optional<ModelPackageItem> findByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long serviceItemId);

    boolean existsByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long itemId);

    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmInOrderByCreatedAtDesc(Long modelId, List<Integer> milestones);

    List<ModelPackageItem> findAllByVehicleModelIdOrderByCreatedAtDesc(Long vehicleModelId);

    @Query("SELECT DISTINCT m.includedSparePart FROM ModelPackageItem m " +
            "WHERE m.vehicleModel.id = :modelId AND m.includedSparePart IS NOT NULL")
    List<SparePart> findDistinctSparePartsByModelId(@Param("modelId") Long modelId);
}