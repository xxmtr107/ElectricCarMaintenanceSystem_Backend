package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.dto.MilestoneConfigDTO;
import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import com.group02.ev_maintenancesystem.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModelPackageItemRepository extends JpaRepository<ModelPackageItem, Long> {

    // --- CÁC HÀM CŨ GIỮ NGUYÊN ---
    List<ModelPackageItem> findByVehicleModelIdOrderByCreatedAtDesc(Long vehicleModelId);

    Optional<ModelPackageItem> findByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long serviceItemId);

    boolean existsByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long itemId);

    @Query("SELECT DISTINCT m.includedSparePart FROM ModelPackageItem m " +
            "WHERE m.vehicleModel.id = :modelId AND m.includedSparePart IS NOT NULL")
    List<SparePart> findDistinctSparePartsByModelId(@Param("modelId") Long modelId);

    // --- CÁC HÀM MỚI CẦN THÊM ---

    // 1. Lấy danh sách cấu hình (Km, Tháng) duy nhất của model, sắp xếp tăng dần
    @Query("SELECT DISTINCT new com.group02.ev_maintenancesystem.dto.MilestoneConfigDTO(m.milestoneKm, m.milestoneMonth) " +
            "FROM ModelPackageItem m " +
            "WHERE m.vehicleModel.id = :modelId " +
            "ORDER BY m.milestoneKm ASC")
    List<MilestoneConfigDTO> findMilestoneConfigsByModelId(@Param("modelId") Long modelId);

    // 2. Tìm items của một mốc cụ thể (dùng cho trường hợp bình thường)
    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(Long modelId, Integer milestoneKm);

    // 3. [QUAN TRỌNG] Tìm items của NHIỀU mốc cùng lúc để GỘP (dùng cho trường hợp lỡ mốc)
    // Sắp xếp theo milestoneKm tăng dần để logic gộp ưu tiên cái sau đè cái trước hợp lý hơn
    List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmInOrderByMilestoneKmAsc(Long modelId, Collection<Integer> milestoneKms);
}