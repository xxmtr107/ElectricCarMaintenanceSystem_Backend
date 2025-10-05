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


    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    User customerUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "technician_id")
    User technicianUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_package_id")
    ServicePackage servicePackage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointment_service_items",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_item_id")
    )
    List<ServiceItem> extraServiceItems = new ArrayList<>();

    @OneToOne(mappedBy = "appointment",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    MaintenanceRecord maintenanceRecord;

}
