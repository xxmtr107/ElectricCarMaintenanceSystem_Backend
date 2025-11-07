package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
