package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Entity này là bảng trung gian có thêm cột, lưu trữ chi tiết
 * của một ServiceItem cụ thể TRONG một Appointment cụ thể.
 * Đây là mấu chốt để giải quyết bài toán Check -> Replace.
 */
@Entity
@Table(name = "appointment_service_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentServiceItemDetail extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = false)
    Appointment appointment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_item_id", nullable = false)
    ServiceItem serviceItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    MaintenanceActionType actionType; // CHECK hoặc REPLACE

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price; // Giá thực tế cho hành động này (đã bao gồm vật tư nếu là REPLACE)

    @Builder.Default
    @Column(name = "customer_approved", nullable = false)
    Boolean customerApproved = true; // Mặc định là true (vì là gói gốc). Nếu Tech upgrade -> false

    @Column(columnDefinition = "NVARCHAR(255)")
    String technicianNotes; // Ghi chú của kỹ thuật viên khi upgrade
}