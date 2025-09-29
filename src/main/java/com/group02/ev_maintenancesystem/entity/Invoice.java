package com.group02.ev_maintenancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice extends BaseEntity {

    @Column(name = "total_amount", precision = 10, scale = 2)
    BigDecimal totalAmount;

    String status;

    // Relationships
    @OneToOne(cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false, unique = true)
    MaintenanceRecord maintenanceRecord;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Payment> payments = new ArrayList<>();
}
