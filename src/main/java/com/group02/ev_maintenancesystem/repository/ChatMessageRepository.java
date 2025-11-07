package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    @Query("SELECT m FROM ChatMessage m WHERE m.room.id = :roomId ORDER BY m.timestamp ASC")
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(@Param("roomId") Long roomId);
}
