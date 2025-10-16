package com.group02.ev_maintenancesystem.entity;

import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity{

    String method;

    BigDecimal amount;

    @Column(name = "payment_date")
    LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    @Column(name = "transaction_code", unique = true)
    String transactionCode;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

}
