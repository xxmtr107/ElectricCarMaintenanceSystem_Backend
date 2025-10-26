package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.repository.MaintenanceRecordRepository;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
import com.group02.ev_maintenancesystem.repository.ServicePackageRepository;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaintenanceServiceImpl implements MaintenanceService {
    VehicleRepository vehicleRepository;
    ServicePackageRepository servicePackageRepository;
    MaintenanceRecordRepository maintenanceRecordRepository;
    ModelPackageItemRepository modelPackageItemRepository;

    @Override
    public List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId) {
        // ... (phần 1, 2, 3 - Lấy xe, lịch sử, kiểm tra quy tắc) ...
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + vehicleId));
        VehicleModel model = vehicle.getModel();
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByVehicle_Id(vehicleId);

        List<MaintenanceRecommendationDTO> duePackages = new ArrayList<>();

        // (Bạn cần sửa hàm 'checkRule' ở dưới trước)
        checkRule(vehicle, history, model.getBasicPackage(), model.getBasicCycleKm(), model.getBasicCycleMonths())
                .ifPresent(duePackages::add);
        checkRule(vehicle, history, model.getStandardPackage(), model.getStandardCycleKm(), model.getStandardCycleMonths())
                .ifPresent(duePackages::add);
        checkRule(vehicle, history, model.getPremiumPackage(), model.getPremiumCycleKm(), model.getPremiumCycleMonths())
                .ifPresent(duePackages::add);
        checkRule(vehicle, history, model.getBatteryPackage(), model.getBatteryCycleKm(), model.getBatteryCycleMonths())
                .ifPresent(duePackages::add);

        // 4. Hợp nhất
        if (duePackages.isEmpty()) {
            return new ArrayList<>();
        }

        MaintenanceRecommendationDTO primaryRecommendation = duePackages.stream()
                .max(Comparator.comparingInt(MaintenanceRecommendationDTO::getDueAtKm))
                .orElse(null);

        if (primaryRecommendation != null) {
            // --- THAY ĐỔI BẮT ĐẦU TỪ ĐÂY ---

            // 1. Lấy danh sách Hạng mục (Entities)
            List<ModelPackageItem> itemEntities = modelPackageItemRepository
                    .findByVehicleModelAndServicePackage(model,
                            servicePackageRepository.findById(primaryRecommendation.getPackageId()).get() // Lấy lại entity Gói
                    );

            // 2. Chuyển đổi (map) Entities sang DTOs
            List<ModelPackageItemDTO> itemDTOs = itemEntities.stream()
                    .map(entity -> {
                        // Map ServiceItem -> ServiceItemDTO
                        ServiceItemDTO itemDTO = new ServiceItemDTO();
                        itemDTO.setName(entity.getServiceItem().getName());
                        itemDTO.setDescription(entity.getServiceItem().getDescription());

                        // Map ModelPackageItem -> ModelPackageItemDTO
                        ModelPackageItemDTO dto = new ModelPackageItemDTO();
                        dto.setServiceItem(itemDTO);
                        dto.setPrice(entity.getPrice());
                        dto.setActionType(entity.getActionType());
                        return dto;
                    })
                    .toList(); // .collect(Collectors.toList())

            // 3. Set danh sách DTO vào kết quả
            primaryRecommendation.setItems(itemDTOs);

            return List.of(primaryRecommendation);
        }

        return new ArrayList<>();
    }

    @Override
    public Optional<MaintenanceRecommendationDTO> checkRule(
            Vehicle vehicle,
            List<MaintenanceRecord> fullHistory,
            ServicePackage packageToCheck,
            Integer cycleKm,
            Integer cycleMonths) {


        // Nếu model này không định nghĩa gói này (ví dụ VF3 không có Gói Pin) -> bỏ qua
        if (packageToCheck == null || cycleKm == null || cycleMonths == null) {
            return Optional.empty();
        }

        // A. Tìm lần cuối cùng gói NÀY được thực hiện
        MaintenanceRecord lastRecordForThisPackage = fullHistory.stream()
                .filter(record -> record.getServicePackage() != null &&
                        record.getServicePackage().getId().equals(packageToCheck.getId()))
                .findFirst()
                .orElse(null);

        // B. Xác định "mốc 0" để bắt đầu tính toán
        LocalDate startDate;
        int startKm;

        if (lastRecordForThisPackage == null) {
            // Xe này chưa bao giờ làm gói này
            startDate = vehicle.getPurchaseYear();
            startKm = 0;
        } else {
            // Xe đã làm gói này ít nhất 1 lần
            startDate = lastRecordForThisPackage.getPerformedAt().toLocalDate();
            startKm = lastRecordForThisPackage.getOdometer();
        }

        // C. Tính toán mốc đến hạn tiếp theo
        LocalDate dueDate = startDate.plusMonths(cycleMonths);
        int dueKm = startKm + cycleKm;

        // D. Kiểm tra điều kiện "Đến hạn"
        // (Lấy số km hiện tại của xe và ngày hôm nay)
        int currentKm = vehicle.getCurrentKm();
        LocalDate today = LocalDate.now();

        boolean isDueByKm = currentKm >= dueKm;
        boolean isDueByDate = today.isAfter(dueDate) || today.isEqual(dueDate);

        // Nếu 1 trong 2 điều kiện đến hạn -> tạo đề xuất
        if (isDueByKm || isDueByDate) {
            String reason = isDueByKm ? "DUE_BY_KM" : "DUE_BY_DATE";

            // Trả về DTO mới, không chứa entity
            MaintenanceRecommendationDTO rec = new MaintenanceRecommendationDTO(
                    packageToCheck.getId(), // Chỉ trả về ID
                    packageToCheck.getName(), // Chỉ trả về Tên
                    dueKm,
                    cycleMonths,
                    reason,
                    null // 'items' sẽ được thêm ở hàm chính
            );
            return Optional.of(rec);
        }

        return Optional.empty();
    }
}
