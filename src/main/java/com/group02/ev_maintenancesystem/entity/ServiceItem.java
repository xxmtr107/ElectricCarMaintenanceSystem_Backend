package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Chi tiết dịch vụ
 */
public class ServiceItem extends BaseEntity {

    @Column(columnDefinition = "NVARCHAR(100)", nullable = false, unique = true)
    String name;

    @JsonIgnore
    @Column(columnDefinition = "NVARCHAR(200)")
    String description;

    @Column(nullable = false)
    BigDecimal price;

    // Relationships
//    @JsonIgnore
//    @ManyToMany(mappedBy = "serviceItemsForPackages", fetch = FetchType.LAZY)
//    List<ServicePackage> servicePackages = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "serviceItems", fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "serviceItems", fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "serviceitem_vehiclemodel",
//            joinColumns = @JoinColumn(name = "service_item_id"),
//            inverseJoinColumns = @JoinColumn(name = "vehicle_model_id")
//    )
//    List<VehicleModel> vehicleModelsForItems = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "serviceItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ModelPackageItem> modelPackageItems = new ArrayList<>();

}
