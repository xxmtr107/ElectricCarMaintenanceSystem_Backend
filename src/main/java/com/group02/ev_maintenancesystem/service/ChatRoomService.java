package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.ChatRoomDTO;
import com.group02.ev_maintenancesystem.dto.RoomMessageDTO;
import com.group02.ev_maintenancesystem.dto.request.CreateRoomRequest;
import com.group02.ev_maintenancesystem.entity.ChatRoom;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface ChatRoomService {
    ChatRoomDTO createChatRoom(CreateRoomRequest request, Principal principal);
    ChatRoomDTO joinChatRoom(Long roomId, Principal principal);
    Set<ChatRoomDTO> getMyRooms(Principal principal);
    List<RoomMessageDTO> getChatHistory(Long roomId, Principal principal);
}
