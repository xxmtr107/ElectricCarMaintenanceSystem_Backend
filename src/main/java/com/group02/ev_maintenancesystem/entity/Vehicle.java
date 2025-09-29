package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vehicle extends BaseEntity{

    @Column(name = "license_plate", nullable = false, unique = true)
    String licensePlate;
    String vin;
    @Column(name = "current_km")
    Integer currentKm = 0;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id", nullable = false)
    VehicleModel model;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
}
