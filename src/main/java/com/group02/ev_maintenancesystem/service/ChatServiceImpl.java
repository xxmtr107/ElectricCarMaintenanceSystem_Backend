package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.ChatMessageDTO;
import com.group02.ev_maintenancesystem.dto.ChatRoomDTO;
import com.group02.ev_maintenancesystem.dto.RoomMessageDTO;
import com.group02.ev_maintenancesystem.entity.ChatMessage;
import com.group02.ev_maintenancesystem.entity.ChatRoom;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.ChatRoomStatus;
import com.group02.ev_maintenancesystem.enums.MessageStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.ChatMessageRepository;
import com.group02.ev_maintenancesystem.repository.ChatRoomRepository;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class ChatServiceImpl implements  ChatService {
    SimpMessagingTemplate simpMessagingTemplate;
    UserRepository userRepository;
    ChatMessageRepository chatMessageRepository;
    ChatRoomRepository chatRoomRepository;
    ModelMapper modelMapper;


    @Override
    public void processAndSendPrivateMessage(ChatMessageDTO dto, Principal principal) {
        //Lấy sender từ principal đã xác thực
        User sender = userRepository.findByUsername(principal.getName()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        //Lấy recipient từ dto
        User recipient = userRepository.findById(dto.getRecipientId())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        String recipientUserName = recipient.getUsername();

        //Hoàn thiện thông tin tin nhắn
        dto.setSenderId(sender.getId());
        dto.setSenderName(sender.getFullName());
        dto.setTimestamp(Instant.now());

        String queueDestination = "/queue/messages";

        //Gửi tin nhắn tới hàng đợi của người nhận
        simpMessagingTemplate.convertAndSendToUser(recipientUserName, queueDestination, dto);
    }

    @Override
    @Transactional
    public void processAndBroadcastRoomMessage(RoomMessageDTO dto, Principal principal) {
        //Tìm user
        User sender = userRepository.findByUsername(principal.getName()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        //Tìm roomchat
        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(()-> new AppException(ErrorCode.ROOM_CHAT_NOT_FOUND));


        ChatMessage messageToSave = ChatMessage.builder()
                .sender(sender)
                .room(room) // Gắn vào phòng
                .content(dto.getContent())
                .status(MessageStatus.SENT)
                .build();

        chatMessageRepository.save(messageToSave);

        //Hoàn thiện dto để gửi
        dto.setSenderId(sender.getId());
        dto.setSenderName(sender.getFullName() != null ? sender.getFullName() : sender.getUsername());
        dto.setTimestamp(messageToSave.getTimestamp());


        // 3. Tạo "kênh" (topic) dựa trên ID của phòng
        String roomTopic = "/topic/chat-room/" + dto.getRoomId();

        // Gửi cho TẤT CẢ MỌI NGƯỜI đang subscribe (lắng nghe) kênh này
        simpMessagingTemplate.convertAndSend(roomTopic, dto);
    }


}
