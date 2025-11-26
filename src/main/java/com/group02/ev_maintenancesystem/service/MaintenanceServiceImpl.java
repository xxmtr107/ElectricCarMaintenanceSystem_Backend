package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.MilestoneConfigDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.MaintenanceRecordRepository;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaintenanceServiceImpl implements MaintenanceService {

    VehicleRepository vehicleRepository;
    MaintenanceRecordRepository maintenanceRecordRepository;
    ModelPackageItemRepository modelPackageItemRepository;

    // Sai số chấp nhận được (ví dụ xe chạy 12.100km vẫn coi là đã làm mốc 12.000km)
    private static final int KM_THRESHOLD_NEARBY = 1000;
    private static final int MONTHLY_THRESHOLD = 12; // Ngưỡng tháng (nếu logic cũ cần)

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        VehicleModel model = vehicle.getModel();
        LocalDate purchaseDate = vehicle.getPurchaseYear();

        if (model == null || purchaseDate == null) {
            return Collections.emptyList();
        }

        Long modelId = model.getId();
        int currentKm = vehicle.getCurrentKm() != null ? vehicle.getCurrentKm() : 0;

        // 1. Lấy danh sách mốc bảo dưỡng ĐỘNG từ Database
        List<MilestoneConfigDTO> milestoneConfigs = modelPackageItemRepository.findMilestoneConfigsByModelId(modelId);

        // Lọc bỏ mốc 1km (mốc nâng cấp)
        milestoneConfigs = milestoneConfigs.stream()
                .filter(m -> m.getMilestoneKm() > 1000)
                .collect(Collectors.toList());

        if (milestoneConfigs.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Tính thời gian
        long monthsSincePurchase = Period.between(purchaseDate, LocalDate.now()).toTotalMonths();

        // 3. Lấy lịch sử
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByAppointment_Vehicle_IdOrderByPerformedAtDesc(vehicleId);

        // 4. Phân nhánh
        if (history.isEmpty()) {
            return handleNoHistoryCase(vehicle, modelId, currentKm, monthsSincePurchase, milestoneConfigs);
        } else {
            return handleWithHistoryCase(vehicle, modelId, currentKm, history, monthsSincePurchase, milestoneConfigs);
        }
    }

    /**
     * KỊCH BẢN 1: Xe chưa bảo dưỡng lần nào
     * Logic: Tìm mốc LỚN NHẤT mà xe đã vượt qua (hoặc chạm tới).
     * Ví dụ: Xe 19k. Mốc [12k, 24k].
     * -> 19k > 12k -> Đã lố mốc 12k -> Phải làm 12k.
     */
    private List<MaintenanceRecommendationDTO> handleNoHistoryCase(
            Vehicle vehicle, Long modelId, int currentKm, long monthsSincePurchase, List<MilestoneConfigDTO> milestones) {

        MilestoneConfigDTO dueMilestone = null;
        String reason = "";

        // Tìm mốc cao nhất mà xe ĐÃ VƯỢT QUA
        for (MilestoneConfigDTO m : milestones) {
            boolean passKm = currentKm >= m.getMilestoneKm();
            boolean passTime = m.getMilestoneMonth() != null && monthsSincePurchase >= m.getMilestoneMonth();

            if (passKm || passTime) {
                dueMilestone = m;
                if (passKm) reason = "DUE_BY_KM";
                else reason = "DUE_BY_TIME";
            } else {
                // Vì list sort ASC, nếu chưa qua mốc này thì chắc chắn chưa qua mốc sau
                break;
            }
        }

        // Nếu xe mới mua (VD: 100km), chưa đến hạn mốc nào (Mốc đầu 12k)
        // -> Trả về rỗng (Hoặc có thể trả về mốc đầu tiên với reason "UPCOMING" tùy bạn, nhưng theo logic Kịch bản 1 thì chưa làm gì)
        if (dueMilestone == null) {
            // Nếu bạn muốn luôn hiển thị mốc đầu tiên cho xe mới:
            // dueMilestone = milestones.get(0);
            // reason = "NEXT_DUE";
            return Collections.emptyList();
        }

        List<ModelPackageItemDTO> items = getItemsForMilestone(modelId, dueMilestone.getMilestoneKm());
        BigDecimal total = calculateTotal(items);

        return List.of(buildRecommendation(dueMilestone.getMilestoneKm(), reason, items, total));
    }

    /**
     * KỊCH BẢN 2 & 3: Đã bảo dưỡng
     * Logic: Đã làm mốc X -> Phải làm mốc Y (Y > X), bất kể ODO hiện tại.
     */
    private List<MaintenanceRecommendationDTO> handleWithHistoryCase(
            Vehicle vehicle, Long modelId, int currentKm, List<MaintenanceRecord> history,
            long monthsSincePurchase, List<MilestoneConfigDTO> milestones) {

        MaintenanceRecord lastRecord = history.get(0);
        int lastOdometer = lastRecord.getOdometer() != null ? lastRecord.getOdometer() : 0;

        // 1. Xác định mốc ĐÃ LÀM
        int finishedMilestoneKm = 0;
        for (MilestoneConfigDTO m : milestones) {
            // Nếu ODO lúc làm gần bằng mốc chuẩn (cho sai số 1000km)
            if (m.getMilestoneKm() <= lastOdometer + KM_THRESHOLD_NEARBY) {
                finishedMilestoneKm = m.getMilestoneKm();
            } else {
                break;
            }
        }

        // 2. Xác định mốc KẾ TIẾP (Target)
        MilestoneConfigDTO nextMilestone = null;
        for (MilestoneConfigDTO m : milestones) {
            if (m.getMilestoneKm() > finishedMilestoneKm) {
                nextMilestone = m;
                break; // Lấy mốc đầu tiên lớn hơn mốc đã làm
            }
        }

        // Nếu đã làm hết tất cả các mốc trong đời xe
        if (nextMilestone == null) {
            return Collections.emptyList();
        }

        // 3. [THAY ĐỔI QUAN TRỌNG] Luôn đề xuất mốc tiếp theo
        // Không chặn bằng điều kiện "dueByKm" nữa

        boolean isDueNow = currentKm >= nextMilestone.getMilestoneKm();
        boolean isDueTime = nextMilestone.getMilestoneMonth() != null && monthsSincePurchase >= nextMilestone.getMilestoneMonth();

        String reason;
        if (isDueNow) {
            reason = "DUE_BY_KM";
        } else if (isDueTime) {
            reason = "DUE_BY_TIME";
        } else {
            // Xe chưa chạy tới mốc (19k < 24k), nhưng hệ thống vẫn gợi ý để khách biết
            reason = "NEXT_MILESTONE";
        }

        List<ModelPackageItemDTO> items = getItemsForMilestone(modelId, nextMilestone.getMilestoneKm());
        BigDecimal total = calculateTotal(items);

        return List.of(buildRecommendation(nextMilestone.getMilestoneKm(), reason, items, total));
    }

    // --- Helpers ---

    private List<ModelPackageItemDTO> getItemsForMilestone(Long modelId, int km) {
        List<ModelPackageItem> entities = modelPackageItemRepository
                .findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(modelId, km);
        return mapEntitiesToDTOs(entities);
    }

    private List<ModelPackageItemDTO> mapEntitiesToDTOs(List<ModelPackageItem> entities) {
        return entities.stream()
                .filter(e -> Boolean.TRUE.equals(e.getActive()))
                .map(this::mapEntityToDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private ModelPackageItemDTO mapEntityToDTO(ModelPackageItem entity) {
        if (entity.getServiceItem() == null) return null;
        ServiceItemDTO itemInfo = ServiceItemDTO.builder()
                .id(entity.getServiceItem().getId())
                .name(entity.getServiceItem().getName())
                .description(entity.getServiceItem().getDescription())
                .build();
        return new ModelPackageItemDTO(itemInfo, entity.getPrice(), entity.getActionType());
    }

    private BigDecimal calculateTotal(List<ModelPackageItemDTO> items) {
        return items.stream()
                .map(ModelPackageItemDTO::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private MaintenanceRecommendationDTO buildRecommendation(Integer milestone, String reason, List<ModelPackageItemDTO> items, BigDecimal total) {
        MaintenanceRecommendationDTO recommendation = new MaintenanceRecommendationDTO();
        recommendation.setMilestoneKm(milestone);
        recommendation.setReason(reason);
        recommendation.setItems(items);
        recommendation.setEstimatedTotal(total);
        return recommendation;
    }
}