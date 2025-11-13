package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.EmailRecord;
import com.group02.ev_maintenancesystem.enums.EmailType;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailRecord, Long> {
    Optional<EmailRecord> findByEmailAndTypeAndSentTimeAfter(String email, EmailType type, LocalDateTime sentTime);

    int countByEmailAndTypeAndCurrentKmAndVehicleID(String email, EmailType type, Integer currentKm, Long vehicleID);
    int countByEmailAndTypeAndAppointmentID(String email, EmailType type, Long id);
    boolean existsByEmailAndTypeAndAppointmentID(String email, EmailType type, Long id);
    int countByEmailAndTypeAndPaymentIDAndStatus(String email, EmailType type, Long id, EmailRecord.MailPaymentStatus status);
    boolean existsByEmailAndTypeAndPaymentIDAndStatus(String email, EmailType type, Long paymentID, EmailRecord.MailPaymentStatus status);
    List<EmailRecord> findByVehicleID(Long vehicleId);
}
