package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO; // Đảm bảo import DTO này
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO; // Đảm bảo import DTO này
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.exception.AppException; // Thêm import AppException
import com.group02.ev_maintenancesystem.exception.ErrorCode; // Thêm import ErrorCode
import com.group02.ev_maintenancesystem.repository.MaintenanceRecordRepository;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
import com.group02.ev_maintenancesystem.repository.ServicePackageRepository; // Giữ lại nếu cần lấy entity Package
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Thêm transactional nếu cần

import java.math.BigDecimal; // Thêm import BigDecimal
import java.time.LocalDate;
import java.time.Period; // Dùng Period để tính toán tháng chính xác hơn
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Thêm Collectors

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaintenanceServiceImpl implements MaintenanceService {
    VehicleRepository vehicleRepository;
    // ServicePackageRepository servicePackageRepository; // Tạm thời có thể không cần nếu chỉ lấy ID
    MaintenanceRecordRepository maintenanceRecordRepository;
    ModelPackageItemRepository modelPackageItemRepository;

    @Override
    @Transactional(readOnly = true) // Nên dùng readOnly cho các hàm chỉ đọc
    public List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId) {
        // 1. Lấy thông tin xe và model
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND)); // Sử dụng AppException
        VehicleModel model = vehicle.getModel();
        if (model == null) {
            // Hoặc throw lỗi hoặc trả về rỗng nếu model bị thiếu
            log.error("Vehicle ID {} is missing VehicleModel association.", vehicleId);
            return new ArrayList<>();
            // Hoặc throw new AppException(ErrorCode.MODEL_NOT_FOUND);
        }


        // 2. Lấy toàn bộ lịch sử bảo dưỡng của xe, sắp xếp theo ngày giảm dần
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByVehicle_IdOrderByPerformedAtDesc(vehicleId); // Thêm OrderBy

        List<MaintenanceRecommendationDTO> duePackages = new ArrayList<>();

        // 3. Kiểm tra từng quy tắc (mốc bảo dưỡng)
        // Sử dụng Optional chaining để gọn hơn
        checkRule(vehicle, history, model.getBasicPackage(), model.getBasicCycleKm(), model.getBasicCycleMonths())
                .ifPresent(duePackages::add);
        checkRule(vehicle, history, model.getStandardPackage(), model.getStandardCycleKm(), model.getStandardCycleMonths())
                .ifPresent(duePackages::add);
        checkRule(vehicle, history, model.getPremiumPackage(), model.getPremiumCycleKm(), model.getPremiumCycleMonths())
                .ifPresent(duePackages::add);
        checkRule(vehicle, history, model.getBatteryPackage(), model.getBatteryCycleKm(), model.getBatteryCycleMonths())
                .ifPresent(duePackages::add);

        // 4. Xác định gói ưu tiên cao nhất (theo km hoặc logic khác nếu cần)
        if (duePackages.isEmpty()) {
            return new ArrayList<>(); // Không có gói nào đến hạn
        }

        // Ưu tiên gói có mốc KM đến hạn cao nhất
        MaintenanceRecommendationDTO primaryRecommendation = duePackages.stream()
                .max(Comparator.comparingInt(MaintenanceRecommendationDTO::getDueAtKm))
                .orElse(null); // Gần như không thể null nếu duePackages không rỗng

        // 5. Lấy chi tiết hạng mục và giá cho gói được đề xuất
        if (primaryRecommendation != null) {
            Long recommendedPackageId = primaryRecommendation.getPackageId();

            // Lấy danh sách ModelPackageItem entities cho model và package này
            List<ModelPackageItem> itemEntities = modelPackageItemRepository
                    .findByVehicleModelIdAndServicePackageId(model.getId(), recommendedPackageId);

            List<ModelPackageItemDTO> itemDTOs; // Khai báo biến ở đây

            if (itemEntities.isEmpty()) {
                log.warn("No ModelPackageItem found for VehicleModel ID {} and ServicePackage ID {}. Recommendation might be incomplete.", model.getId(), recommendedPackageId);
                itemDTOs = new ArrayList<>(); // Gán list rỗng nếu không tìm thấy
                primaryRecommendation.setItems(itemDTOs);
            } else {
                // Chuyển đổi Entities sang DTOs
                itemDTOs = itemEntities.stream() // Gán giá trị cho itemDTOs
                        .map(entity -> {
                            ServiceItem serviceItem = entity.getServiceItem();
                            ServiceItemDTO itemInfoDTO = null;
                            if (serviceItem != null) {
                                itemInfoDTO = new ServiceItemDTO();
                                itemInfoDTO.setName(serviceItem.getName());
                                itemInfoDTO.setDescription(serviceItem.getDescription());
                            } else {
                                log.warn("ModelPackageItem ID {} is missing ServiceItem association.", entity.getId());
                            }

                            ModelPackageItemDTO dto = new ModelPackageItemDTO();
                            dto.setServiceItem(itemInfoDTO);
                            dto.setPrice(entity.getPrice());
                            dto.setActionType(entity.getActionType());
                            return dto;
                        })
                        .collect(Collectors.toList());

                // Set danh sách DTO vào kết quả
                primaryRecommendation.setItems(itemDTOs);
            }

            // ----> DI CHUYỂN ĐOẠN TÍNH TỔNG XUỐNG ĐÂY <----
            // Tính tổng giá ước tính từ các item DTOs
            BigDecimal estimatedTotal = itemDTOs.stream()
                    .map(ModelPackageItemDTO::getPrice)
                    .filter(java.util.Objects::nonNull) // Bỏ qua nếu giá là null
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Bạn có thể thêm trường `estimatedTotal` vào MaintenanceRecommendationDTO nếu muốn
            // Ví dụ: primaryRecommendation.setEstimatedTotal(estimatedTotal);
            // (Cần thêm trường `estimatedTotal` vào class MaintenanceRecommendationDTO trước)

            log.info("Calculated estimated total for recommendation: {}", estimatedTotal); // Log giá trị tính được

            return List.of(primaryRecommendation);
        }

        return new ArrayList<>();// Trường hợp không tìm thấy gói ưu tiên (khó xảy ra)
    }

    // Hàm checkRule cần được cập nhật để tìm đúng bản ghi cuối cùng của GÓI ĐÓ
    @Override
    public Optional<MaintenanceRecommendationDTO> checkRule(
            Vehicle vehicle,
            List<MaintenanceRecord> fullHistorySortedDesc, // Lịch sử đã sắp xếp giảm dần
            ServicePackage packageToCheck,
            Integer cycleKm,
            Integer cycleMonths) {

        // Nếu model không định nghĩa gói này hoặc thiếu thông tin chu kỳ -> bỏ qua
        if (packageToCheck == null || cycleKm == null || cycleMonths == null || cycleKm <= 0 || cycleMonths <= 0) {
            // log.debug("Skipping check for package ID {} on model {} due to missing config.", packageToCheck != null ? packageToCheck.getId() : "null", vehicle.getModel().getName());
            return Optional.empty();
        }

        Long packageIdToCheck = packageToCheck.getId();

        // A. Tìm lần cuối cùng gói NÀY được thực hiện từ lịch sử đã sắp xếp
        MaintenanceRecord lastRecordForThisPackage = fullHistorySortedDesc.stream()
                .filter(record -> record.getServicePackage() != null &&
                        record.getServicePackage().getId().equals(packageIdToCheck))
                .findFirst() // Lấy bản ghi đầu tiên (mới nhất) khớp với gói
                .orElse(null);

        // B. Xác định "mốc 0" để bắt đầu tính toán
        LocalDate startDate;
        int startKm;

        if (lastRecordForThisPackage == null) {
            // Xe này chưa bao giờ làm gói này -> Tính từ ngày mua xe và 0km
            startDate = vehicle.getPurchaseYear(); // Giả sử purchaseYear là LocalDate
            if (startDate == null) {
                log.error("Vehicle ID {} is missing Purchase Year. Cannot calculate due date.", vehicle.getId());
                return Optional.empty(); // Không thể tính nếu thiếu ngày mua
            }
            startKm = 0;
        } else {
            // Xe đã làm gói này -> Tính từ ngày làm và số km lúc đó
            startDate = lastRecordForThisPackage.getPerformedAt().toLocalDate();
            startKm = lastRecordForThisPackage.getOdometer();
            if (startKm < 0) startKm = 0; // Đảm bảo không bị âm km
        }

        // C. Tính toán mốc đến hạn tiếp theo
        // LocalDate dueDate = startDate.plusMonths(cycleMonths); // Cách tính này có thể không chính xác hoàn toàn về số ngày
        LocalDate dueDate = startDate.plus(Period.ofMonths(cycleMonths)); // Chính xác hơn
        int dueKm = startKm + cycleKm;

        // D. Kiểm tra điều kiện "Đến hạn"
        int currentKm = vehicle.getCurrentKm();
        LocalDate today = LocalDate.now();

        // Điều kiện đến hạn: Số km hiện tại VƯỢT QUÁ hoặc BẰNG mốc km HOẶC ngày hiện tại SAU hoặc BẰNG ngày đến hạn
        boolean isDueByKm = currentKm >= dueKm;
        // boolean isDueByDate = today.isAfter(dueDate) || today.isEqual(dueDate);
        boolean isDueByDate = !today.isBefore(dueDate); // Cách viết khác tương đương

        String reason = null;
        if (isDueByKm && isDueByDate) {
            reason = "DUE_BY_KM_AND_DATE";
        } else if (isDueByKm) {
            reason = "DUE_BY_KM";
        } else if (isDueByDate) {
            reason = "DUE_BY_DATE";
        }

        // Nếu đến hạn -> tạo đề xuất DTO (chưa cần kèm items ở đây)
        if (reason != null) {
            // log.info("Recommendation found for Vehicle ID {}, Package ID {}. Reason: {}", vehicle.getId(), packageIdToCheck, reason);
            MaintenanceRecommendationDTO rec = new MaintenanceRecommendationDTO(
                    packageToCheck.getId(),
                    packageToCheck.getName(),
                    dueKm, // Mốc km đến hạn
                    cycleMonths, // Chu kỳ tháng (để tham khảo)
                    reason,
                    null // items sẽ được thêm ở hàm getRecommendations
            );
            return Optional.of(rec);
        } else {
            // log.debug("Package ID {} is not due yet for Vehicle ID {}. Current KM: {}, Due KM: {}. Today: {}, Due Date: {}", packageIdToCheck, vehicle.getId(), currentKm, dueKm, today, dueDate);
        }

        return Optional.empty(); // Chưa đến hạn
    }

    // Thêm hàm query mới vào MaintenanceRecordRepository interface:
    // List<MaintenanceRecord> findByVehicle_IdOrderByPerformedAtDesc(Long vehicleId);

}