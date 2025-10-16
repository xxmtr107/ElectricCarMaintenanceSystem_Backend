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
    String name;

    @Column(name = "model_year")
    String modelYear;

    @Column(name = "basic_maintenance", precision = 5, scale = 2)
    Integer basicMaintenance = 10000; // Bảo dưỡng cơ bản (km)

    @Column(name = "comprehensive_maintenance", precision = 6, scale = 2)
    Integer comprehensiveMaintenance = 20000; // Bảo dưỡng toàn diện (km)

    @Column(name = "basic_maintenance_time")
    Integer basicMaintenanceTime = 6; // Bảo dưỡng cơ bản (tháng)

    @Column(name = "comprehensive_maintenance_time")
    Integer comprehensiveMaintenanceTime = 12; // Bảo dưỡng toàn diện (tháng)

    @OneToMany (mappedBy = "model", fetch = FetchType.LAZY)
    List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "vehicleModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ModelPackageItem> modelPackageItems = new ArrayList<>();

}
