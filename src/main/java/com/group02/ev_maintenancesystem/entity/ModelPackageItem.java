package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "model_package_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelPackageItem extends BaseEntity {

    @Column(nullable = false)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_model_id", nullable = false)
    VehicleModel vehicleModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_package_id")
    ServicePackage servicePackage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_item_id")
    ServiceItem serviceItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    MaintenanceActionType actionType;

}
