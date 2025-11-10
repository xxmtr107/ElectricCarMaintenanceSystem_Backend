package com.group02.ev_maintenancesystem.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomMessageDTO {
    // Client gửi lên
    String content;
    Long roomId;

    // Server điền vào
    Long senderId;
    String senderName;
    Instant timestamp;
}
