package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.ChatMessageDTO;
import com.group02.ev_maintenancesystem.dto.RoomMessageDTO;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.entity.ChatMessage;
import com.group02.ev_maintenancesystem.entity.ChatRoom;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.MessageStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.service.ChatRoomService;
import com.group02.ev_maintenancesystem.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    ChatService chatService;
    //CHAT 1-1
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO, Principal principal){
        chatService.processAndSendPrivateMessage(chatMessageDTO, principal);
    }


    //ROOM CHAT
    @MessageMapping("/chat.sendRoomMessage")
    public void sendRoomMessage(@Payload RoomMessageDTO dto, Principal principal){
        chatService.processAndBroadcastRoomMessage(dto, principal);
    }

}
