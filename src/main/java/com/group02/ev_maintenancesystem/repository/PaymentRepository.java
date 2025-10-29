    package com.group02.ev_maintenancesystem.repository;

    import com.group02.ev_maintenancesystem.entity.Payment;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;
@Repository
    public interface PaymentRepository extends JpaRepository<Payment, Long> {
        Optional<Payment> findByTransactionCode(String transactionCode);
    }
