package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(columnDefinition = "NVARCHAR(200)")
    String description;


    //Relationships
//    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
//    List<ServiceItem> serviceItems = new ArrayList<>();

    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "servicepackage_vehiclemodel",
//            joinColumns = @JoinColumn(name = "service_package_id"),
//            inverseJoinColumns = @JoinColumn(name = "vehicle_model_id")
//    )
//    List<VehicleModel> vehicleModelsForPackages = new ArrayList<>();
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "servicepackage_serviceitem",
//            joinColumns = @JoinColumn(name = "service_package_id"),
//            inverseJoinColumns = @JoinColumn(name = "service_item_id")
//    )
//    List<ServiceItem> serviceItemsForPackages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "servicePackage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ModelPackageItem> modelPackageItems = new ArrayList<>();
}
