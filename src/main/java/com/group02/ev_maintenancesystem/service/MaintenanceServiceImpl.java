package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendation;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.repository.MaintenanceRecordRepository;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
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
    MaintenanceRecordRepository maintenanceRecordRepository;
    ModelPackageItemRepository modelPackageItemRepository;

    @Override
    public List<MaintenanceRecommendation> getRecommendations(Long vehicleId) {
        // 1. Lấy thông tin xe và model
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + vehicleId));
        VehicleModel model = vehicle.getModel();

        // 2. Lấy toàn bộ lịch sử bảo dưỡng của xe này
        List<MaintenanceRecord> history = maintenanceRecordRepository.findByVehicle_Id(vehicleId);

        // 3. Kiểm tra từng quy tắc (rule) một cách độc lập
        List<MaintenanceRecommendation> duePackages = new ArrayList<>();

        // Kiểm tra Gói Cơ bản
        checkRule(vehicle, history, model.getBasicPackage(), model.getBasicCycleKm(), model.getBasicCycleMonths())
                .ifPresent(duePackages::add);

        // Kiểm tra Gói Tiêu chuẩn
        checkRule(vehicle, history, model.getStandardPackage(), model.getStandardCycleKm(), model.getStandardCycleMonths())
                .ifPresent(duePackages::add);

        // Kiểm tra Gói Cao cấp
        checkRule(vehicle, history, model.getPremiumPackage(), model.getPremiumCycleKm(), model.getPremiumCycleMonths())
                .ifPresent(duePackages::add);

        // Kiểm tra Gói Pin
        checkRule(vehicle, history, model.getBatteryPackage(), model.getBatteryCycleKm(), model.getBatteryCycleMonths())
                .ifPresent(duePackages::add);

        // 4. Hợp nhất (Consolidate) kết quả
        if (duePackages.isEmpty()) {
            return new ArrayList<>(); // Không có gì đến hạn
        }

        // Nếu có nhiều gói cùng đến hạn (ví dụ: Cơ bản và Tiêu chuẩn cùng đến hạn ở 24k km),
        // chúng ta chỉ chọn gói "lớn nhất" (có chu kỳ km cao nhất).
        MaintenanceRecommendation primaryRecommendation = duePackages.stream()
                .max(Comparator.comparingInt(MaintenanceRecommendation::getDueAtKm))
                .orElse(null);

        // Lấy danh sách hạng mục chi tiết cho gói lớn nhất này
        if (primaryRecommendation != null) {
            List<ModelPackageItem> items = modelPackageItemRepository
                    .findByVehicleModelAndServicePackage(model, primaryRecommendation.getRecommendedPackage());
            primaryRecommendation.setItems(items);

            return List.of(primaryRecommendation); // Chỉ trả về 1 đề xuất duy nhất, bao hàm nhất
        }

        return new ArrayList<>();
    }

    @Override
    public Optional<MaintenanceRecommendation> checkRule(
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

            // (Chúng ta chưa lấy 'items' ở đây, sẽ lấy ở hàm chính để tối ưu)
            MaintenanceRecommendation rec = new MaintenanceRecommendation(
                    packageToCheck, dueKm, cycleMonths, reason, null
            );
            return Optional.of(rec);
        }

        return Optional.empty(); // Chưa đến hạn // Chưa đến hạn
    }
}
