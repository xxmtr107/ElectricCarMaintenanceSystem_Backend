package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.EmailRecord;
import com.group02.ev_maintenancesystem.enums.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailRecord, Long> {
    Optional<EmailRecord> findByEmailAndTypeAndSentTimeAfter(String email, EmailType type, LocalDateTime sentTime);
    int countByEmailAndType(String email, EmailType type);
}
