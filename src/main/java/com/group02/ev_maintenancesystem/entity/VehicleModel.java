package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicle_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleModel extends  BaseEntity {
    @Column(nullable = false, unique = true)
    String name;

    @Column(name = "model_year")
    String modelYear;

    // --- THAY ĐỔI: HỆ THỐNG QUY TẮC BẢO DƯỠNG ---
    // Xóa các cột 'basicMaintenance', 'comprehensiveMaintenance' cũ.
    // Thay bằng hệ thống liên kết rõ ràng.

    // --- QUY TẮC CHO GÓI 1 (ID=1: Bảo dưỡng cơ bản) ---
    @Column(name = "basic_cycle_km")
    Integer basicCycleKm; // Chu kỳ km (ví dụ: 12000)

    @Column(name = "basic_cycle_months")
    Integer basicCycleMonths; // Chu kỳ tháng (ví dụ: 12)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "basic_package_id")
    ServicePackage basicPackage; // Liên kết tới ServicePackage(ID=1)

    // --- QUY TẮC CHO GÓI 2 (ID=2: Bảo dưỡng tiêu chuẩn) ---
    @Column(name = "standard_cycle_km")
    Integer standardCycleKm; // Chu kỳ km (ví dụ: 24000)

    @Column(name = "standard_cycle_months")
    Integer standardCycleMonths; // Chu kỳ tháng (ví dụ: 24)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "standard_package_id")
    ServicePackage standardPackage; // Liên kết tới ServicePackage(ID=2)

    // --- QUY TẮC CHO GÓI 3 (ID=3: Bảo dưỡng cao cấp) ---
    @Column(name = "premium_cycle_km")
    Integer premiumCycleKm; // Chu kỳ km (ví dụ: 48000)

    @Column(name = "premium_cycle_months")
    Integer premiumCycleMonths; // Chu kỳ tháng (ví dụ: 48)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "premium_package_id")
    ServicePackage premiumPackage; // Liên kết tới ServicePackage(ID=3)

    // --- QUY TẮC CHO GÓI 4 (ID=4: Bảo dưỡng Pin) ---
    @Column(name = "battery_cycle_km")
    Integer batteryCycleKm; // Chu kỳ km (ví dụ: 48000)

    @Column(name = "battery_cycle_months")
    Integer batteryCycleMonths; // Chu kỳ tháng (ví dụ: 48)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "battery_package_id")
    ServicePackage batteryPackage; // Liên kết tới ServicePackage(ID=4)
    @OneToMany (mappedBy = "model", fetch = FetchType.LAZY)
    List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "vehicleModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ModelPackageItem> modelPackageItems = new ArrayList<>();

}
