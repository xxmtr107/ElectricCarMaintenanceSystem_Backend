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

    @OneToOne(mappedBy = "maintenanceRecord", orphanRemoval = true)
    Invoice invoice;

    @OneToMany(mappedBy = "maintenanceRecord",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<PartUsage> partUsages = new ArrayList<>();

}
