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

    @Column(name = "name", nullable = false, length = 255)
    String name;

    String address;

    @Column(name = "ward", length = 100) // Cột Phường/Xã
    String ward;

    @Column(name = "district", nullable = false, length = 100) // Cột Quận/Huyện
    String district;

    @Column(name = "city", nullable = false, length = 100) // Cột Tỉnh/Thành phố
    String city;

    String phone;

    @OneToMany(mappedBy = "serviceCenter")
    List<User> users;

    @OneToMany(mappedBy = "serviceCenter")
    List<Appointment> appointments;

    @OneToMany(mappedBy = "serviceCenter")
    List<Invoice> invoices;

    @OneToMany(mappedBy = "serviceCenter")
    List<MaintenanceRecord> maintenanceRecords;
}
