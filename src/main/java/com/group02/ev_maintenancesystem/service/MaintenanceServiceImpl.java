package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType; // Thêm import
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
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function; // Thêm import
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
        // 1. Lấy thông tin xe cơ bản
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

        // 2. Lấy lịch sử bảo dưỡng (sắp xếp giảm dần theo ngày)
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByAppointment_Vehicle_IdOrderByPerformedAtDesc(vehicleId);

        // ==========================================================
        // === LOGIC MỚI: Xử lý dựa trên việc có lịch sử hay không ===
        // ==========================================================

        if (history.isEmpty()) {
            // ----- TRƯỜNG HỢP 1: CHƯA CÓ LỊCH SỬ BẢO DƯỠNG -----
            return handleNoHistoryCase(vehicle, modelId, currentKm, purchaseDate);
        } else {
            // ----- TRƯỜNG HỢP 2: ĐÃ CÓ LỊCH SỬ BẢO DƯỠNG -----
            return handleWithHistoryCase(vehicle, modelId, currentKm, history, purchaseDate);
        }
    }

    // --- Hàm xử lý khi chưa có lịch sử ---
    private List<MaintenanceRecommendationDTO> handleNoHistoryCase(Vehicle vehicle, Long modelId, int currentKm, LocalDate purchaseDate) {
        log.debug("Handling recommendation for Vehicle ID {} with no history. Current KM: {}", vehicle.getId(), currentKm);

        // 1. Tìm tất cả các mốc chuẩn <= KM hiện tại
        List<Integer> missedMilestones = STANDARD_MILESTONES.stream()
                .filter(m -> m <= currentKm)
                .collect(Collectors.toList());

        // 2. Tính số tháng từ ngày mua
        Period periodSincePurchase = Period.between(purchaseDate, LocalDate.now());
        long monthsSincePurchase = periodSincePurchase.toTotalMonths();
        boolean timeThresholdMet = monthsSincePurchase >= MONTHLY_THRESHOLD;

        // 3. Xác định mốc cao nhất bị bỏ lỡ (hoặc mốc 12k nếu đến hạn do thời gian)
        Integer milestoneToRecommend = null;
        String reason = "";

        if (!missedMilestones.isEmpty()) {
            // Nếu có mốc KM bị bỏ lỡ, chọn mốc cao nhất
            milestoneToRecommend = missedMilestones.get(missedMilestones.size() - 1);
            reason = timeThresholdMet ? "MISSED_MILESTONES_KM_TIME" : "MISSED_MILESTONES_KM";
            log.info("Vehicle ID {}: Missed milestones up to {}. Recommending highest missed: {}. Reason: {}",
                    vehicle.getId(), milestoneToRecommend, milestoneToRecommend, reason);
        } else if (timeThresholdMet) {
            // Chưa đến mốc 12k nhưng đã đủ 12 tháng
            milestoneToRecommend = STANDARD_MILESTONES.get(0); // Đề xuất mốc đầu tiên (12000)
            reason = "DUE_BY_TIME";
            missedMilestones = List.of(milestoneToRecommend); // Coi như mốc 12k bị "miss" do thời gian
            log.info("Vehicle ID {}: Under {}km but {} months passed. Recommending first milestone: {}. Reason: {}",
                    vehicle.getId(), STANDARD_MILESTONES.get(0), monthsSincePurchase, milestoneToRecommend, reason);
        }

        // 4. Nếu không có mốc nào cần đề xuất (chưa đủ KM và chưa đủ tháng)
        if (milestoneToRecommend == null) {
            log.info("Vehicle ID {}: Not due by KM ({}) or Time ({} months). No recommendation.",
                    vehicle.getId(), currentKm, monthsSincePurchase);
            return Collections.emptyList();
        }

        // 5. Lấy và gộp hạng mục từ TẤT CẢ các mốc bị bỏ lỡ
        List<ModelPackageItemDTO> combinedItems = getCombinedItemsForMilestones(modelId, missedMilestones);

        if (combinedItems.isEmpty()) {
            log.warn("No items found for missed milestones {} for Model ID {}. Cannot generate recommendation.",
                    missedMilestones, modelId);
            return Collections.emptyList();
        }

        // 6. Tính tổng giá
        BigDecimal estimatedTotal = calculateTotal(combinedItems);

        // 7. Tạo đề xuất
        MaintenanceRecommendationDTO recommendation = buildRecommendation(milestoneToRecommend, reason, combinedItems, estimatedTotal);
        return List.of(recommendation);
    }

    // --- Hàm xử lý khi đã có lịch sử ---
    private List<MaintenanceRecommendationDTO> handleWithHistoryCase(Vehicle vehicle, Long modelId, int currentKm, List<MaintenanceRecord> history, LocalDate purchaseDate) {
        log.debug("Handling recommendation for Vehicle ID {} with history. Current KM: {}", vehicle.getId(), currentKm);

        // 1. Xác định ngày/km tham chiếu từ lần bảo dưỡng cuối
        MaintenanceRecord lastRecord = history.get(0); // Lịch sử đã sort DESC
        LocalDateTime referenceDateTime = lastRecord.getPerformedAt() != null ? lastRecord.getPerformedAt() : purchaseDate.atStartOfDay(); // Fallback to purchase date if needed
        int lastOdometer = lastRecord.getOdometer() != null ? lastRecord.getOdometer() : 0;

        // Xác định mốc chuẩn gần nhất <= odometer của lần bảo dưỡng cuối
        int referenceKm = STANDARD_MILESTONES.stream()
                .filter(m -> m <= lastOdometer + KM_THRESHOLD_NEARBY)
                .max(Integer::compareTo)
                .orElse(0);
        log.debug("Vehicle ID {}: Last service at {}km (recorded odometer {}). Reference milestone set to {}km.",
                vehicle.getId(), lastOdometer, lastOdometer, referenceKm);

        // 2. Tính số tháng từ lần bảo dưỡng cuối
        Period periodSinceLast = Period.between(referenceDateTime.toLocalDate(), LocalDate.now());
        long monthsPassed = periodSinceLast.toTotalMonths();

        // 3. Xác định mốc KM cần kiểm tra tiếp theo (mốc đầu tiên > referenceKm)
        Integer nextMilestoneInSequence = STANDARD_MILESTONES.stream()
                .filter(m -> m > referenceKm)
                .min(Integer::compareTo)
                .orElse(null);

        if (nextMilestoneInSequence == null) {
            log.info("Vehicle ID {}: Seems to have completed or passed all standard milestones based on last service KM {}.", vehicle.getId(), referenceKm);
            return Collections.emptyList();
        }
        log.debug("Vehicle ID {}: Next milestone in sequence after {}km is {}km.", vehicle.getId(), referenceKm, nextMilestoneInSequence);

        // 4. Kiểm tra điều kiện đến hạn
        boolean dueByKm = currentKm >= nextMilestoneInSequence;
        boolean dueByTime = monthsPassed >= MONTHLY_THRESHOLD;
        String reason = "";
        Integer milestoneToRecommend = null;

        if (dueByKm || dueByTime) { // Chỉ cần 1 điều kiện đúng
            milestoneToRecommend = nextMilestoneInSequence;
            if (dueByKm && dueByTime) {
                reason = "DUE_BY_KM_AND_TIME";
            } else if (dueByKm) {
                reason = "DUE_BY_KM";
            } else {
                reason = "DUE_BY_TIME";
            }
            log.info("Vehicle ID {}: Due for milestone {}km. Reason: {}", vehicle.getId(), milestoneToRecommend, reason);
        } else {
            log.info("Vehicle ID {}: Not due for next milestone {}km yet. (Current KM: {}, Months since last: {})",
                    vehicle.getId(), nextMilestoneInSequence, currentKm, monthsPassed);
            return Collections.emptyList(); // Không đến hạn
        }

        // 5. Lấy hạng mục cho mốc đến hạn
        List<ModelPackageItem> itemEntities = modelPackageItemRepository
                .findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(modelId, milestoneToRecommend);

        if (itemEntities.isEmpty()) {
            log.warn("No ModelPackageItem found for VehicleModel ID {} at milestone {}km. Cannot generate recommendation.", modelId, milestoneToRecommend);
            return Collections.emptyList();
        }

        // 6. Chuyển đổi sang DTOs
        List<ModelPackageItemDTO> itemDTOs = mapEntitiesToDTOs(itemEntities);

        // 7. Tính tổng giá
        BigDecimal estimatedTotal = calculateTotal(itemDTOs);

        // 8. Tạo đề xuất
        MaintenanceRecommendationDTO recommendation = buildRecommendation(milestoneToRecommend, reason, itemDTOs, estimatedTotal);
        return List.of(recommendation);
    }

    // --- Hàm helper: Lấy và gộp hạng mục cho các mốc bị bỏ lỡ ---
    private List<ModelPackageItemDTO> getCombinedItemsForMilestones(Long modelId, List<Integer> milestones) {
        if (milestones == null || milestones.isEmpty()) {
            return Collections.emptyList();
        }

        // Lấy tất cả hạng mục cho các mốc liên quan
        List<ModelPackageItem> allRelevantItems = modelPackageItemRepository.findByVehicleModelIdAndMilestoneKmInOrderByCreatedAtDesc(modelId, milestones); // Cần thêm phương thức này vào Repo

        // Gộp hạng mục, ưu tiên REPLACE
        Map<Long, ModelPackageItemDTO> combinedMap = new LinkedHashMap<>(); // Dùng LinkedHashMap để giữ thứ tự nếu cần

        for (ModelPackageItem entity : allRelevantItems) {
            ServiceItem serviceItem = entity.getServiceItem();
            if (serviceItem == null) {
                log.warn("ModelPackageItem ID {} is missing ServiceItem association during combination.", entity.getId());
                continue; // Bỏ qua nếu thiếu liên kết
            }
            Long itemId = serviceItem.getId();

            ModelPackageItemDTO currentDto = mapEntityToDTO(entity); // Tạo DTO cho entity hiện tại

            // Kiểm tra xem item đã có trong map chưa
            ModelPackageItemDTO existingDto = combinedMap.get(itemId);

            if (existingDto == null) {
                // Nếu chưa có, thêm vào map
                combinedMap.put(itemId, currentDto);
            } else {
                // Nếu đã có, ưu tiên REPLACE
                if (currentDto.getActionType() == MaintenanceActionType.REPLACE) {
                    // Cập nhật giá và action type của cái đã có thành REPLACE
                    existingDto.setActionType(MaintenanceActionType.REPLACE);
                    // Cập nhật giá bằng giá mới nhất (hoặc giá cao nhất?) - Chọn giá mới nhất từ entity hiện tại
                    existingDto.setPrice(currentDto.getPrice());
                }
                // Nếu cái hiện tại là CHECK và cái đã có cũng là CHECK, không cần làm gì
                // Nếu cái hiện tại là CHECK và cái đã có là REPLACE, giữ nguyên REPLACE
            }
        }
        return new ArrayList<>(combinedMap.values());
    }
     /*
     Trong ModelPackageItemRepository.java, thêm:
     List<ModelPackageItem> findByVehicleModelIdAndMilestoneKmIn(Long modelId, List<Integer> milestones);
     */


    // --- Hàm helper: Map List<Entity> sang List<DTO> ---
    private List<ModelPackageItemDTO> mapEntitiesToDTOs(List<ModelPackageItem> entities) {
        return entities.stream()
                .map(this::mapEntityToDTO)
                .filter(Objects::nonNull) // Lọc bỏ null nếu mapEntityToDTO trả về null
                .collect(Collectors.toList());
    }

    // --- Hàm helper: Map một Entity sang DTO ---
    private ModelPackageItemDTO mapEntityToDTO(ModelPackageItem entity) {
        ServiceItem serviceItem = entity.getServiceItem();
        ServiceItemDTO itemInfoDTO = null;
        if (serviceItem != null) {
            itemInfoDTO = new ServiceItemDTO(serviceItem.getId(), serviceItem.getName(), serviceItem.getDescription());
        } else {
            log.warn("ModelPackageItem ID {} is missing ServiceItem association.", entity.getId());
            return null; // Trả về null nếu thiếu thông tin quan trọng
        }
        return new ModelPackageItemDTO(itemInfoDTO, entity.getPrice(), entity.getActionType());
    }


    // --- Hàm helper: Tính tổng giá ---
    private BigDecimal calculateTotal(List<ModelPackageItemDTO> items) {
        return items.stream()
                .map(ModelPackageItemDTO::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // --- Hàm helper: Tạo đối tượng Recommendation DTO ---
    private MaintenanceRecommendationDTO buildRecommendation(Integer milestone, String reason, List<ModelPackageItemDTO> items, BigDecimal total) {
        MaintenanceRecommendationDTO recommendation = new MaintenanceRecommendationDTO();
        recommendation.setMilestoneKm(milestone);
        recommendation.setReason(reason);
        recommendation.setItems(items);
        recommendation.setEstimatedTotal(total);
        return recommendation;
    }

    // Hàm checkRule không còn dùng
}