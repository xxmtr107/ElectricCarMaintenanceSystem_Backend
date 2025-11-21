package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ChatRoom;
import com.group02.ev_maintenancesystem.enums.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    //Tìm phòng bởi status của phòng chat
    List<ChatRoom> findByStatus(ChatRoomStatus status);
    //Tìm phòng cho dù là staff hay customer
    List<ChatRoom> findByCustomerIdOrStaffId(Long customerId, Long staffId);
}
