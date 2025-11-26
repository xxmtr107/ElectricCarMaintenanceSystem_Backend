package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.MilestoneConfigDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
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

    // Ngưỡng sai số Km cho phép (ví dụ: làm lúc 12.100km vẫn tính là mốc 12.000km)
    private static final int KM_THRESHOLD_NEARBY = 1000;

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId) {
        // 1. Lấy thông tin xe
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        VehicleModel model = vehicle.getModel();
        LocalDate purchaseDate = vehicle.getPurchaseYear();

        if (model == null || purchaseDate == null) {
            return Collections.emptyList();
        }

        Long modelId = model.getId();
        int currentKm = vehicle.getCurrentKm() != null ? vehicle.getCurrentKm() : 0;

        // 2. Lấy danh sách cấu hình mốc (Dynamic Config) từ DB
        List<MilestoneConfigDTO> milestoneConfigs = modelPackageItemRepository.findMilestoneConfigsByModelId(modelId);

        // Lọc bỏ mốc 1km (nếu có) vì đây thường là mốc cấu hình nâng cấp/sửa chữa, không phải lịch định kỳ
        milestoneConfigs = milestoneConfigs.stream()
                .filter(m -> m.getMilestoneKm() > 1000)
                .collect(Collectors.toList());

        if (milestoneConfigs.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. Tính thời gian sử dụng xe (tháng)
        long monthsSincePurchase = Period.between(purchaseDate, LocalDate.now()).toTotalMonths();

        // 4. Lấy lịch sử bảo dưỡng gần nhất
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByAppointment_Vehicle_IdOrderByPerformedAtDesc(vehicleId);

        // 5. Phân nhánh xử lý
        if (history.isEmpty()) {
            return handleNoHistoryCase(vehicle, modelId, currentKm, monthsSincePurchase, milestoneConfigs);
        } else {
            return handleWithHistoryCase(vehicle, modelId, currentKm, history, monthsSincePurchase, milestoneConfigs);
        }
    }

    /**
     * KỊCH BẢN 1: Xe chưa bảo dưỡng lần nào
     * Logic: Tìm tất cả các mốc đã bị vượt qua (do Km hoặc Thời gian) -> Gộp lại thành 1 gói.
     * Ví dụ: Xe đi 37.000km -> Lỡ 12k, 24k, 36k -> Gộp hết lại.
     */
    private List<MaintenanceRecommendationDTO> handleNoHistoryCase(
            Vehicle vehicle, Long modelId, int currentKm, long monthsSincePurchase, List<MilestoneConfigDTO> milestones) {

        List<Integer> missedMilestones = new ArrayList<>();
        MilestoneConfigDTO highestMilestone = null;
        String reason = "";

        // Duyệt qua các mốc để tìm những mốc đã đến hạn
        for (MilestoneConfigDTO m : milestones) {
            boolean passKm = currentKm >= m.getMilestoneKm();
            boolean passTime = m.getMilestoneMonth() != null && monthsSincePurchase >= m.getMilestoneMonth();

            if (passKm || passTime) {
                missedMilestones.add(m.getMilestoneKm());
                highestMilestone = m; // Cập nhật mốc cao nhất

                if (passKm && passTime) reason = "DUE_BY_KM_AND_TIME";
                else if (passKm) reason = "DUE_BY_KM";
                else reason = "DUE_BY_TIME";
            } else {
                // Vì list đã sort ASC, nếu chưa qua mốc này thì chắc chắn chưa qua mốc sau -> Dừng
                break;
            }
        }

        // Nếu xe mới mua chưa đến hạn mốc nào
        if (missedMilestones.isEmpty()) {
            // Có thể trả về gợi ý mốc đầu tiên với reason "UPCOMING" nếu muốn, hoặc trả rỗng
            return Collections.emptyList();
        }

        // [GỘP HẠNG MỤC] Lấy item của tất cả các mốc bị lỡ và xử lý ưu tiên
        List<ModelPackageItemDTO> combinedItems = getCombinedItemsForMilestones(modelId, missedMilestones);
        BigDecimal total = calculateTotal(combinedItems);

        return List.of(buildRecommendation(highestMilestone.getMilestoneKm(), reason, combinedItems, total));
    }

    /**
     * KỊCH BẢN 2 & 3: Xe đã có lịch sử bảo dưỡng
     * Cập nhật: Xử lý trường hợp xe "nhảy cóc" (Ví dụ: làm 6k xong chạy tới 25k mới quay lại)
     */
    private List<MaintenanceRecommendationDTO> handleWithHistoryCase(
            Vehicle vehicle, Long modelId, int currentKm, List<MaintenanceRecord> history,
            long monthsSincePurchase, List<MilestoneConfigDTO> milestones) {

        MaintenanceRecord lastRecord = history.get(0);

        // 1. Xác định mốc ĐÃ LÀM (finishedMilestone)
        int finishedMilestoneKm = 0;
        if (lastRecord.getAppointment() != null && lastRecord.getAppointment().getMilestoneKm() != null) {
            finishedMilestoneKm = lastRecord.getAppointment().getMilestoneKm();
        } else {
            int lastOdometer = lastRecord.getOdometer() != null ? lastRecord.getOdometer() : 0;
            for (MilestoneConfigDTO m : milestones) {
                if (m.getMilestoneKm() <= lastOdometer + KM_THRESHOLD_NEARBY) {
                    finishedMilestoneKm = m.getMilestoneKm();
                } else { break; }
            }
        }

        // 2. Tìm tất cả các mốc trong tương lai (Lớn hơn mốc đã làm)
        int finalFinishedMilestoneKm = finishedMilestoneKm;
        List<MilestoneConfigDTO> futureMilestones = milestones.stream()
                .filter(m -> m.getMilestoneKm() > finalFinishedMilestoneKm)
                .collect(Collectors.toList());

        if (futureMilestones.isEmpty()) {
            return Collections.emptyList(); // Đã làm hết các mốc
        }

        // 3. Phân loại: Tìm các mốc "Bị lỡ" (Missed/Due) dựa trên ODO hiện tại
        List<Integer> missedMilestoneKms = new ArrayList<>();
        MilestoneConfigDTO highestDueMilestone = null;
        String reason = "NEXT_MILESTONE";

        for (MilestoneConfigDTO m : futureMilestones) {
            boolean isDueKm = currentKm >= m.getMilestoneKm();
            boolean isDueTime = m.getMilestoneMonth() != null && monthsSincePurchase >= m.getMilestoneMonth();

            if (isDueKm || isDueTime) {
                // Nếu xe đã chạy qua mốc này -> Thêm vào danh sách cần làm gộp
                missedMilestoneKms.add(m.getMilestoneKm());
                highestDueMilestone = m;

                if (isDueKm) reason = "DUE_BY_KM";
                else reason = "DUE_BY_TIME";
            } else {
                // Chưa chạy tới mốc này -> Dừng kiểm tra các mốc sau
                break;
            }
        }

        // TRƯỜNG HỢP A: Xe chạy vượt cấp (Có mốc bị lỡ)
        // Ví dụ: Làm 6k -> Chạy tới 25k. Các mốc bị lỡ: 12k, 18k, 24k (giả sử mốc là 6k, 12k, 18k, 24k)
        if (!missedMilestoneKms.isEmpty()) {
            // Gộp item của 12k, 18k, 24k lại
            List<ModelPackageItemDTO> combinedItems = getCombinedItemsForMilestones(modelId, missedMilestoneKms);
            BigDecimal total = calculateTotal(combinedItems);

            // Đề xuất mốc cao nhất (24k)
            return List.of(buildRecommendation(highestDueMilestone.getMilestoneKm(), reason, combinedItems, total));
        }

        // TRƯỜNG HỢP B: Xe chạy bình thường (Chưa đến hạn mốc kế tiếp)
        // Ví dụ: Làm 6k -> Chạy tới 8k. Mốc kế là 12k.
        MilestoneConfigDTO nextImmediate = futureMilestones.get(0);
        List<ModelPackageItemDTO> items = getItemsForMilestone(modelId, nextImmediate.getMilestoneKm());
        BigDecimal total = calculateTotal(items);

        return List.of(buildRecommendation(nextImmediate.getMilestoneKm(), "NEXT_MILESTONE", items, total));
    }

    /**
     * LOGIC GỘP HẠNG MỤC (CORE LOGIC)
     * Input: Danh sách mốc bị lỡ (VD: 12k, 24k, 36k)
     * Output: Danh sách item duy nhất, ưu tiên REPLACE > CHECK
     */
    private List<ModelPackageItemDTO> getCombinedItemsForMilestones(Long modelId, List<Integer> milestones) {
        // Lấy tất cả item từ DB của các mốc này, sắp xếp Km tăng dần
        List<ModelPackageItem> allItems = modelPackageItemRepository.findByVehicleModelIdAndMilestoneKmInOrderByMilestoneKmAsc(modelId, milestones);

        Map<Long, ModelPackageItemDTO> combinedMap = new HashMap<>();

        for (ModelPackageItem item : allItems) {
            // Bỏ qua item đã bị vô hiệu hóa (Inactive)
            if (!Boolean.TRUE.equals(item.getActive())) continue;
            if (item.getServiceItem() != null && !Boolean.TRUE.equals(item.getServiceItem().getActive())) continue;

            Long serviceId = item.getServiceItem().getId();
            ModelPackageItemDTO newItemDTO = mapEntityToDTO(item);

            if (combinedMap.containsKey(serviceId)) {
                ModelPackageItemDTO existingDTO = combinedMap.get(serviceId);

                // Logic ưu tiên:
                // 1. Nếu item mới là REPLACE -> Luôn lấy (Nâng cấp từ Check lên Replace, hoặc cập nhật giá Replace mới hơn)
                if (newItemDTO.getActionType() == MaintenanceActionType.REPLACE) {
                    combinedMap.put(serviceId, newItemDTO);
                }
                // 2. Nếu item mới là CHECK
                else {
                    // Chỉ lấy nếu cái cũ cũng là CHECK (cập nhật giá/thông tin mới của lần check này)
                    // Nếu cái cũ đang là REPLACE -> GIỮ NGUYÊN REPLACE (Không hạ cấp xuống Check)
                    if (existingDTO.getActionType() == MaintenanceActionType.CHECK) {
                        combinedMap.put(serviceId, newItemDTO);
                    }
                }
            } else {
                // Chưa có thì thêm mới
                combinedMap.put(serviceId, newItemDTO);
            }
        }
        return new ArrayList<>(combinedMap.values());
    }

    // --- Helper Methods ---

    private List<ModelPackageItemDTO> getItemsForMilestone(Long modelId, int km) {
        return modelPackageItemRepository.findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(modelId, km)
                .stream()
                .filter(e -> Boolean.TRUE.equals(e.getActive())) // Chỉ lấy Active
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

        // Tính toán thông tin Due (tham khảo)
        // recommendation.setDueAtKm(...);
        // recommendation.setDueAtMonths(...);

        return recommendation;
    }
}