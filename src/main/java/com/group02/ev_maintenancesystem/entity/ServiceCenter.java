package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "name", length = 255)
    String name;

    String address;


    @Column(name = "district", length = 100) // Cột Quận/Huyện
    String district;

    @Column(name = "city", length = 100) // Cột Tỉnh/Thành phố
    String city;

    String phone;

    @OneToMany(mappedBy = "serviceCenter", fetch = FetchType.LAZY)
    List<User> users;

    @OneToMany(mappedBy = "serviceCenter", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Appointment> appointments;

    @OneToMany(mappedBy = "serviceCenter", fetch = FetchType.LAZY)
    List<Invoice> invoices;
}
