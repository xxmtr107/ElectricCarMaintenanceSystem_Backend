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
    private static final int MONTHLY_THRESHOLD = 12; // Ngưỡng tháng (ví dụ: 12 tháng)

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId) {
        // 1. Lấy thông tin xe, model, km, ngày mua
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        VehicleModel model = vehicle.getModel();
        LocalDate purchaseDate = vehicle.getPurchaseYear(); // Giả sử đây là LocalDate

        if (model == null || purchaseDate == null) {
            log.error("Vehicle ID {} is missing VehicleModel or Purchase Year association.", vehicleId);
            return Collections.emptyList();
        }
        Long modelId = model.getId();
        int currentKm = vehicle.getCurrentKm() != null ? vehicle.getCurrentKm() : 0;

        // 2. Lấy lịch sử và xác định ngày tham chiếu cho việc tính thời gian
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByVehicle_IdOrderByPerformedAtDesc(vehicleId);
        LocalDateTime referenceDateTime = history.stream()
                .map(MaintenanceRecord::getPerformedAt)
                .filter(Objects::nonNull)
                .findFirst() // Lấy ngày của bản ghi mới nhất
                .orElse(purchaseDate.atStartOfDay()); // Nếu không có lịch sử, lấy ngày mua

        // 3. Tính số tháng đã trôi qua
        Period period = Period.between(referenceDateTime.toLocalDate(), LocalDate.now());
        long monthsPassed = period.toTotalMonths();

        // 4. Tìm cột mốc KM đến hạn tiếp theo (hoặc mốc hiện tại nếu chưa làm)
        Integer nextMilestoneBasedOnKm = STANDARD_MILESTONES.stream()
                .filter(milestone -> milestone > currentKm)
                .min(Integer::compareTo)
                .orElse(null);

        Integer currentMilestoneExactMatch = STANDARD_MILESTONES.stream()
                .filter(m -> m == currentKm).findFirst().orElse(null);

        Integer effectiveMilestone = nextMilestoneBasedOnKm; // Mặc định là mốc tiếp theo
        boolean dueByKm = false;
        String reason = "";

        // Kiểm tra xem có cần bảo dưỡng ngay tại mốc hiện tại không
        if (currentMilestoneExactMatch != null) {
            boolean alreadyDoneAroundCurrent = history.stream()
                    .anyMatch(r -> r.getOdometer() != null && Math.abs(r.getOdometer() - currentKm) < 1000); // Sai số 1000km
            if (!alreadyDoneAroundCurrent) {
                effectiveMilestone = currentMilestoneExactMatch; // Ưu tiên mốc hiện tại nếu chưa làm
                dueByKm = true; // Đến hạn do KM (đang ở đúng mốc)
                log.info("Vehicle ID {} is at milestone {}km and hasn't been serviced recently. Setting this as effective milestone.", vehicleId, currentKm);
            }
        }
        // Nếu không phải mốc hiện tại cần làm, kiểm tra mốc tiếp theo
        else if (nextMilestoneBasedOnKm != null && currentKm >= nextMilestoneBasedOnKm) {
            // Mặc dù logic tìm nextMilestone > currentKm, nhưng để chắc chắn
            // Hoặc logic này nên là: currentKm đã tiến gần đến nextMilestone (ví dụ: trong vòng 1000km)
            // Tạm thời giữ logic đơn giản: chỉ kích hoạt khi currentKm >= effectiveMilestone (nếu effective = next)
            // --> Logic này chưa ổn, phải là currentKm >= milestone *trước đó* + chu kỳ.
            // --> Quay lại logic đơn giản nhất: Tìm mốc ĐẦU TIÊN >= currentKm mà chưa làm.

            // Tìm mốc đầu tiên >= currentKm
            Integer firstMilestoneDue = STANDARD_MILESTONES.stream()
                    .filter(m -> m >= currentKm)
                    .min(Integer::compareTo)
                    .orElse(null);

            if(firstMilestoneDue != null) {
                // Kiểm tra xem mốc này đã làm gần đây chưa
                final int finalMilestone = firstMilestoneDue; // Biến final cho lambda
                boolean alreadyDoneAroundMilestone = history.stream()
                        .anyMatch(r -> r.getOdometer() != null && Math.abs(r.getOdometer() - finalMilestone) < 1000);

                if (!alreadyDoneAroundMilestone) {
                    effectiveMilestone = firstMilestoneDue;
                    dueByKm = true; // Đến hạn vì đã đạt hoặc vượt mốc này mà chưa làm
                    log.info("Vehicle ID {} reached/passed milestone {}km and hasn't been serviced recently. Setting this as effective milestone.", vehicleId, firstMilestoneDue);
                } else if (nextMilestoneBasedOnKm != null) {
                    // Đã làm mốc hiện tại/vừa qua, xem xét mốc tiếp theo
                    effectiveMilestone = nextMilestoneBasedOnKm;
                    dueByKm = false; // Chưa đến hạn KM cho mốc *tiếp theo* này
                    log.info("Vehicle ID {} serviced around {}km. Considering next milestone {}.", vehicleId, firstMilestoneDue, nextMilestoneBasedOnKm);
                } else {
                    // Đã làm mốc cuối cùng hoặc đã qua hết mốc
                    effectiveMilestone = null;
                }
            } else {
                // Đã vượt qua tất cả các mốc
                effectiveMilestone = null;
            }
        } else if (nextMilestoneBasedOnKm != null) {
            // Chưa đến mốc KM tiếp theo
            effectiveMilestone = nextMilestoneBasedOnKm;
            dueByKm = false;
        } else {
            // Đã vượt qua tất cả các mốc
            effectiveMilestone = null;
        }


        // 5. Kiểm tra đến hạn do thời gian
        boolean dueByTime = monthsPassed >= MONTHLY_THRESHOLD;

        // 6. Xác định lý do và Mốc cuối cùng để đề xuất
        Integer milestoneToRecommend = null;
        if (dueByKm && dueByTime) {
            milestoneToRecommend = effectiveMilestone;
            reason = "DUE_BY_KM_AND_TIME";
        } else if (dueByKm) {
            milestoneToRecommend = effectiveMilestone;
            reason = "DUE_BY_KM";
        } else if (dueByTime) {
            // Nếu chỉ đến hạn do thời gian, đề xuất mốc KM hiệu lực (có thể là mốc sắp tới)
            milestoneToRecommend = effectiveMilestone;
            reason = "DUE_BY_TIME";
        }

        // 7. Nếu có mốc cần đề xuất (do KM hoặc Time)
        if (milestoneToRecommend != null) {
            // Lấy hạng mục cho mốc này
            List<ModelPackageItem> itemEntities = modelPackageItemRepository
                    .findByVehicleModelIdAndMilestoneKm(modelId, milestoneToRecommend);

            if (itemEntities.isEmpty()) {
                log.warn("No ModelPackageItem found for VehicleModel ID {} at milestone {}km. Cannot generate recommendation.", modelId, milestoneToRecommend);
                return Collections.emptyList();
            }

            // Chuyển đổi sang DTOs
            List<ModelPackageItemDTO> itemDTOs = itemEntities.stream()
                    .map(entity -> {
                        ServiceItem serviceItem = entity.getServiceItem();
                        ServiceItemDTO itemInfoDTO = null;
                        if (serviceItem != null) {
                            itemInfoDTO = new ServiceItemDTO(
                                    serviceItem.getId(),
                                    serviceItem.getName(),
                                    serviceItem.getDescription()
                            );
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

            log.info("Generated recommendation for Vehicle ID {} at milestone {}km (Reason: {}) with {} items, total {}.",
                    vehicleId, milestoneToRecommend, reason, itemDTOs.size(), estimatedTotal);

            return List.of(recommendation);
        }

        // Không có mốc nào đến hạn do KM hoặc Time
        log.info("No maintenance milestone due for Vehicle ID {} (Current KM: {}, Months since last service/purchase: {}).",
                vehicleId, currentKm, monthsPassed);
        return Collections.emptyList();
    }

    // Hàm checkRule không còn dùng
}