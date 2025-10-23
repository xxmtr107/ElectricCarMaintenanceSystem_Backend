package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "service_centers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceCenter extends BaseEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false, length = 200)
    String address;

    @Column(length = 100)
    String city;

    @Column(length = 15)
    String phone;

    @Column(length = 100)
    String email;

    @Column(length = 100)
    String managerName;

    @Column(length = 255)
    String description;

    @OneToMany(mappedBy = "serviceCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<User> users;

    @OneToMany(mappedBy = "serviceCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Appointment> appointments;

    @OneToMany(mappedBy = "serviceCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Invoice> invoices;

    @OneToMany(mappedBy = "serviceCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords;
}
