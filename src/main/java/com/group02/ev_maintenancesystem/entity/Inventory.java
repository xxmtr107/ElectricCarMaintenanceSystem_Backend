package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "inventories", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"service_center_id", "spare_part_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Inventory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_center_id", nullable = false)
    ServiceCenter serviceCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spare_part_id", nullable = false)
    SparePart sparePart;

    @Column(name = "quantity", nullable = false)
    Integer quantity; // Số lượng tồn kho tại trung tâm này

    @Column(name = "min_stock", nullable = false, columnDefinition = "int default 5")
    Integer minStock; // Mức cảnh báo tối thiểu tại trung tâm này
}