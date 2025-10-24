package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maintenance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Hồ sơ bảo dưỡng đã thực hiện
 */
public class MaintenanceRecord extends  BaseEntity {


    Integer odometer; // Số km đã đi được tại thời điểm bảo dưỡng

    @Column(name = "performed_at")
    LocalDateTime performedAt; // Thời gian bảo dưỡng


    // Relationships
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id")
    Appointment appointment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "technician_id")
    User technicianUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_package_id")
    ServicePackage servicePackage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "record_service_items",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "service_item_id")
    )
    List<ServiceItem> serviceItems = new ArrayList<>();


    @OneToOne(mappedBy = "maintenanceRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Invoice invoice;

    @OneToMany(mappedBy = "maintenanceRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<PartUsage> partUsages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "center_id")
    ServiceCenter serviceCenter;

}
