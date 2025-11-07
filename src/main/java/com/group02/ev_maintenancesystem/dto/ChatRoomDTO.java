package com.group02.ev_maintenancesystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoomDTO {
    Long id;
    String name;
    String status;
    Set<UserDTO> members;
}
