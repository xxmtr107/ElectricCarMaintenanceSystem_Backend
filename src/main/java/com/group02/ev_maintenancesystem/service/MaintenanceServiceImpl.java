package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
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
import java.time.LocalDateTime; // Thêm import
import java.time.Period; // Thêm import
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

    private static final List<Integer> STANDARD_MILESTONES = Arrays.asList(
            12000, 24000, 36000, 48000, 60000,
            72000, 84000, 96000, 108000, 120000
    );
    private static final int MONTHLY_THRESHOLD = 12; // Ngưỡng tháng
    private static final int KM_THRESHOLD_NEARBY = 1000; // Ngưỡng KM coi là đã làm gần mốc

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId) {
        // 1. Lấy thông tin xe
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        VehicleModel model = vehicle.getModel();
        LocalDate purchaseDate = vehicle.getPurchaseYear();

        if (model == null || purchaseDate == null) {
            log.error("Vehicle ID {} is missing VehicleModel or Purchase Year association.", vehicleId);
            return Collections.emptyList();
        }
        Long modelId = model.getId();
        int currentKm = vehicle.getCurrentKm() != null ? vehicle.getCurrentKm() : 0;

        // 2. Lấy lịch sử và xác định ngày/km tham chiếu
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByVehicle_IdOrderByPerformedAtDesc(vehicleId);
        LocalDateTime referenceDateTime = purchaseDate.atStartOfDay();
        int referenceKm; // Mốc KM đã hoàn thành gần nhất

        if (!history.isEmpty()) {
            MaintenanceRecord lastRecord = history.get(0);
            if (lastRecord.getPerformedAt() != null) {
                referenceDateTime = lastRecord.getPerformedAt();
            }
            if (lastRecord.getOdometer() != null && lastRecord.getOdometer() > 0) {
                // Tìm mốc chuẩn gần nhất <= odometer của lần bảo dưỡng cuối
                final int lastOdometer = lastRecord.getOdometer();
                referenceKm = STANDARD_MILESTONES.stream()
                        .filter(m -> m <= lastOdometer + KM_THRESHOLD_NEARBY) // Tìm mốc gần nhất đã qua
                        .max(Integer::compareTo)
                        .orElse(0); // Nếu lần bảo dưỡng cuối < 12k thì coi như chưa qua mốc nào
            } else {
                referenceKm = 0;
            }
        } else {
            referenceKm = 0;
        }

        // 3. Tính số tháng đã trôi qua
        Period period = Period.between(referenceDateTime.toLocalDate(), LocalDate.now());
        long monthsPassed = period.toTotalMonths();

        // 4. Xác định mốc KM cần kiểm tra tiếp theo (mốc đầu tiên > referenceKm)
        Integer nextMilestoneInSequence = STANDARD_MILESTONES.stream()
                .filter(m -> m > referenceKm)
                .min(Integer::compareTo)
                .orElse(null); // Null nếu đã hoàn thành/vượt qua mốc cuối cùng

        if (nextMilestoneInSequence == null) {
            log.info("Vehicle ID {} seems to have completed or passed all standard milestones based on last service KM {}.", vehicleId, referenceKm);
            return Collections.emptyList(); // Không còn mốc nào trong chuỗi
        }

        // 5. Kiểm tra điều kiện đến hạn
        boolean dueByKm = currentKm >= nextMilestoneInSequence;
        boolean dueByTime = monthsPassed >= MONTHLY_THRESHOLD;
        String reason = "";
        Integer milestoneToRecommend = null;

        if (dueByKm && dueByTime) {
            milestoneToRecommend = nextMilestoneInSequence;
            reason = "DUE_BY_KM_AND_TIME";
        } else if (dueByKm) {
            milestoneToRecommend = nextMilestoneInSequence;
            reason = "DUE_BY_KM";
        } else if (dueByTime) {
            // Đến hạn do thời gian -> Đề xuất mốc tiếp theo trong chuỗi
            milestoneToRecommend = nextMilestoneInSequence;
            reason = "DUE_BY_TIME";
        }

        // 6. Nếu có mốc cần đề xuất
        if (milestoneToRecommend != null) {
            // Lấy hạng mục cho mốc này
            List<ModelPackageItem> itemEntities = modelPackageItemRepository
                    .findByVehicleModelIdAndMilestoneKm(modelId, milestoneToRecommend);

            if (itemEntities.isEmpty()) {
                log.warn("No ModelPackageItem found for VehicleModel ID {} at milestone {}km. Cannot generate recommendation.", modelId, milestoneToRecommend);
                // CÓ THỂ THỬ TÌM HẠNG MỤC CỦA MỐC GẦN NHẤT THẤP HƠN NẾU MỐC NÀY KHÔNG CÓ? (Logic phức tạp hơn)
                return Collections.emptyList();
            }

            // Chuyển đổi sang DTOs
            List<ModelPackageItemDTO> itemDTOs = itemEntities.stream()
                    .map(entity -> {
                        ServiceItem serviceItem = entity.getServiceItem();
                        ServiceItemDTO itemInfoDTO = null;
                        if (serviceItem != null) {
                            itemInfoDTO = new ServiceItemDTO(serviceItem.getId(), serviceItem.getName(), serviceItem.getDescription());
                        } else {
                            log.warn("ModelPackageItem ID {} is missing ServiceItem association.", entity.getId());
                        }
                        return new ModelPackageItemDTO(itemInfoDTO, entity.getPrice(), entity.getActionType());
                    })
                    .filter(dto -> dto.getServiceItem() != null)
                    .collect(Collectors.toList());

            // Tính tổng giá
            BigDecimal estimatedTotal = itemDTOs.stream()
                    .map(ModelPackageItemDTO::getPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Tạo đề xuất
            MaintenanceRecommendationDTO recommendation = new MaintenanceRecommendationDTO();
            recommendation.setMilestoneKm(milestoneToRecommend);
            recommendation.setReason(reason);
            recommendation.setItems(itemDTOs);
            recommendation.setEstimatedTotal(estimatedTotal);
            // Bỏ các trường không còn dùng
            // recommendation.setDueAtKm(0);
            // recommendation.setDueAtMonths(0);

            log.info("Generated recommendation for Vehicle ID {} at milestone {}km (Reason: {}) with {} items, total {}.",
                    vehicleId, milestoneToRecommend, reason, itemDTOs.size(), estimatedTotal);

            return List.of(recommendation);
        }

        // Không đến hạn
        log.info("No maintenance milestone due for Vehicle ID {} (Current KM: {}, Last Service KM around: {}, Months since last service/purchase: {}). Next milestone: {}",
                vehicleId, currentKm, referenceKm, monthsPassed, nextMilestoneInSequence != null ? nextMilestoneInSequence : "None");
        return Collections.emptyList();
    }

    // Hàm checkRule không còn dùng
}