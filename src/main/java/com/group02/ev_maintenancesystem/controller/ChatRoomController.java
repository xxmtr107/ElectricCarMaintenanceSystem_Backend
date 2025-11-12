package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.ChatMessageDTO;
import com.group02.ev_maintenancesystem.dto.ChatRoomDTO;
import com.group02.ev_maintenancesystem.dto.RoomMessageDTO;
import com.group02.ev_maintenancesystem.dto.request.CreateRoomRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    @Autowired
    ChatRoomService chatRoomService;

    @PostMapping("/create")
    public ApiResponse<ChatRoomDTO> createChatRoom(@RequestBody CreateRoomRequest chatRoom, Principal principal){
        return ApiResponse.<ChatRoomDTO>builder()
                .message("Create chat room successfully")
                .result(chatRoomService.createChatRoom(chatRoom, principal))
                .build();
    }

    @PostMapping("/{roomId}/join")
    public ApiResponse<ChatRoomDTO> joinChatRoom(@PathVariable Long roomId, Principal principal) {
        return ApiResponse.<ChatRoomDTO>builder()
                .message("Joined chat room successfully")
                .result(chatRoomService.joinChatRoom(roomId, principal))
                .build();
    }

    @GetMapping("/my-room")
    public ApiResponse<Set<ChatRoomDTO>> getMyRoom(Principal principal){
        return ApiResponse.<Set<ChatRoomDTO>>builder()
                .message("Get chat rooms successfully")
                .result(chatRoomService.getMyRooms(principal))
                .build();
    }

    @GetMapping("/{roomId}/message")
    public ApiResponse<List<RoomMessageDTO>> getChatHistory(@PathVariable Long roomId, Principal principal){
        return ApiResponse.<List<RoomMessageDTO>>builder()
                .message("Get chat history successfully")
                .result(chatRoomService.getChatHistory(roomId, principal))
                .build();
    }
    @GetMapping("/pending")
    public ApiResponse<List<ChatRoomDTO>> getPendingRooms() {
        return ApiResponse.<List<ChatRoomDTO>>builder()
                .message("Get pending rooms successfully")
                .result(chatRoomService.getPendingRooms())
                .build();
    }


}