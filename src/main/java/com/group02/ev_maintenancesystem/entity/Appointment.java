package com.group02.ev_maintenancesystem.entity;



import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
    @Column(nullable = false, name = "appointment_date")
    LocalDateTime appointmentDate;

    Integer milestoneKm;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @Column(nullable = false, name = "estimated_cost", precision = 10, scale = 2)
    BigDecimal estimatedCost; // Giá ước tính

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    User customerUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "technician_id")
    User technicianUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

    @Column(name = "service_package_name")
    String servicePackageName;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    List<AppointmentServiceItemDetail> serviceDetails = new ArrayList<>();

    @OneToOne(mappedBy = "appointment", fetch = FetchType.EAGER)
    MaintenanceRecord maintenanceRecord;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "center_id")
    ServiceCenter serviceCenter;

    public void addServiceDetail(AppointmentServiceItemDetail detail) {
        serviceDetails.add(detail);
        detail.setAppointment(this);
    }

    // Helper method to recalculate total
    public void recalculateEstimatedCost() {
        this.estimatedCost = this.serviceDetails.stream()
                .filter(AppointmentServiceItemDetail::getCustomerApproved)
                .map(AppointmentServiceItemDetail::getPrice)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
