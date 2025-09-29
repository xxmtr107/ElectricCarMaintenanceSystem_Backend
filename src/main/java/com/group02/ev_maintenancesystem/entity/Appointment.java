package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment extends BaseEntity {
    LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @Column(columnDefinition = "NVARCHAR(200)")
    String notes;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "technician_id", nullable = false)
    Technician technician;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    Vehicle vehicle;

    @OneToOne(mappedBy = "appointment",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    MaintenanceRecord maintenanceRecord;



}
