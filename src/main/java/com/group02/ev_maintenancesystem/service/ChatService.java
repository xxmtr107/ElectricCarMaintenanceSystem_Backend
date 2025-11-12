package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.ChatMessageDTO;
import com.group02.ev_maintenancesystem.dto.ChatRoomDTO;
import com.group02.ev_maintenancesystem.dto.RoomMessageDTO;

import java.security.Principal;
import java.util.List;

public interface ChatService {
    void processAndSendPrivateMessage(ChatMessageDTO dto, Principal principal);
    void processAndBroadcastRoomMessage(RoomMessageDTO dto, Principal principal);

}
