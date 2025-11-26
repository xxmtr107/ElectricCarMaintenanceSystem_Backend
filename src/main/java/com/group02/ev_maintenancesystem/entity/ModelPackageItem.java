package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
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

    @Column(name = "milestone_km", nullable = false)
    Integer milestoneKm;

    @Column(name = "milestone_month")
    Integer milestoneMonth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_item_id")
    ServiceItem serviceItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    MaintenanceActionType actionType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "included_spare_part_id") // Thêm cột này vào CSDL
    SparePart includedSparePart;

    // Số lượng phụ tùng được bao gồm (mặc định là 1 nếu có)
    @Builder.Default
    @Column(name = "included_quantity", columnDefinition = "int default 1")
    Integer includedQuantity = 1;
}
