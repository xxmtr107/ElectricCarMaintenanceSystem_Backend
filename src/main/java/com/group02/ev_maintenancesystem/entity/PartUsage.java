package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "part_usages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Bảng PartUsage là bảng trung gian, nối giữa MaintenanceRecord và SparePart. Nó lưu chi tiết:
 * Sử dụng phụ tùng nào.
 * Số lượng bao nhiêu.
 * Giá tại thời điểm sử dụng.
 * Tổng tiền.
 */
public class PartUsage extends BaseEntity {
    @Column(name = "quantity_used", nullable = false)
    Integer quantityUsed; //Số lượng phụ tùng đã sử dụng

    @Column(name = "total_price", nullable = false)
    BigDecimal totalPrice; //Tổng giá tiền = SparePart.unitPrice * quantityUsed

    //Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spare_part_id", nullable = false)
    SparePart sparePart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    MaintenanceRecord maintenanceRecord;

    public void caculateTotalPrice() {
        if (quantityUsed != null && sparePart != null && sparePart.getUnitPrice() != null) {
            this.totalPrice = sparePart.getUnitPrice().multiply(BigDecimal.valueOf(quantityUsed));
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }
}
