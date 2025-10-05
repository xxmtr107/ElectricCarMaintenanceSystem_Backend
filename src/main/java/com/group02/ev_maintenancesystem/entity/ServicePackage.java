package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Loại dịch vụ (Bảo dưỡng sơ cấp , Bảo dưỡng trung cấp , Bảo dưỡng cao cấp)
 */
public class ServicePackage extends BaseEntity {
    @Column(columnDefinition = "NVARCHAR(100)", nullable = false)
    String name;

    String description;

    @Column(nullable = false)
    BigDecimal price;

    //Relationships
//    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
//    List<ServiceItem> serviceItems = new ArrayList<>();

    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "servicepackage_vehiclemodel",
            joinColumns = @JoinColumn(name = "service_package_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_model_id")
    )
    List<VehicleModel> vehicleModels = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "servicepackage_serviceitem",
            joinColumns = @JoinColumn(name = "service_package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_item_id")
    )
    List<ServiceItem> includedServiceItems = new ArrayList<>();
}
