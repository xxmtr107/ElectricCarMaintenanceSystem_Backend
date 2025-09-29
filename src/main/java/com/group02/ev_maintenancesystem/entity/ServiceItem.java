package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Chi tiết dịch vụ
 */
public class ServiceItem extends BaseEntity {
    String name;
    String description;
    BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_type_id", nullable = false)
    ServiceType serviceType;

    @ManyToMany(mappedBy = "serviceItems", fetch = FetchType.LAZY)
    List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
}
