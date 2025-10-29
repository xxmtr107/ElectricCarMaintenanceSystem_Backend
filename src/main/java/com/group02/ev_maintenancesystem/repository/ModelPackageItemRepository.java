package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import com.group02.ev_maintenancesystem.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelPackageItemRepository extends JpaRepository<ModelPackageItem, Long> {


    List<ModelPackageItem> findByVehicleModelId(Long vehicleModelId);

//    List<ModelPackageItem> findByServicePackageId(Long servicePackageId);
//
//    List<ModelPackageItem> findByVehicleModelIdAndServicePackageId(Long modelId, Long servicePackageId);
//
//    List<ModelPackageItem> findByVehicleModelIdAndServicePackageIsNull(Long vehicleModelId);
//
//    Optional<ModelPackageItem> findByVehicleModelIdAndServicePackageIsNullAndServiceItemId(Long modelId, Long serviceItemId);
//
//    Optional<ModelPackageItem> findByVehicleModelIdAndServicePackageIdAndServiceItemId(Long modelId, Long packageId, Long itemId);
//
//    boolean existsByVehicleModelIdAndServicePackageIdAndServiceItemId(Long modelId, Long packageId, Long itemId);
//
//    boolean existsByVehicleModelIdAndServicePackageIsNullAndServiceItemId(Long modelId, Long itemId);
//
//    List<ModelPackageItem> findByVehicleModelAndServicePackage(VehicleModel model, ServicePackage recommendedPackage);
//
//    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKm(Long modelId, Integer milestoneToRecommend);
    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKm(Long modelId, Integer milestoneKm);

    Optional<ModelPackageItem> findByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long serviceItemId);

    boolean existsByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long itemId);

    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmIn(Long modelId, List<Integer> milestones);
}
