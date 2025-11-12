package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ChatRoom;
import com.group02.ev_maintenancesystem.enums.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByStatus(ChatRoomStatus status);
}
