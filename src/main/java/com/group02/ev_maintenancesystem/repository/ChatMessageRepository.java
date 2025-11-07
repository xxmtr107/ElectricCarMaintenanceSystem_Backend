package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
