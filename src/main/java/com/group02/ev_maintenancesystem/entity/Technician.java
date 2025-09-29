package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "technicians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Technician extends  BaseEntity {

    String fullname;

    String specialization;

    Integer experienceYears;

    //Relationships

    @OneToOne(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @OneToMany(mappedBy = "technician", fetch = FetchType.EAGER)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Appointment> appointments = new ArrayList<>();

}
