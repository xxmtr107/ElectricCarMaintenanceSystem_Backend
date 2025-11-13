package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.ChatRoomDTO;
import com.group02.ev_maintenancesystem.dto.RoomMessageDTO;
import com.group02.ev_maintenancesystem.dto.request.CreateRoomRequest;
import com.group02.ev_maintenancesystem.entity.ChatMessage;
import com.group02.ev_maintenancesystem.entity.ChatRoom;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.ChatRoomStatus;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
//@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomServiceImpl implements  ChatRoomService {
    ChatRoomRepository chatRoomRepository;
    UserRepository userRepository;
    ChatMessageRepository chatMessageRepository;
    ModelMapper modelMapper;
    SimpMessagingTemplate simpMessagingTemplate;
    private static final String STAFF_LOBBY_TOPIC = "/topic/staff-lobby";

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository,
                               UserRepository userRepository,
                               ChatMessageRepository chatMessageRepository,
                               ModelMapper modelMapper,
                               SimpMessagingTemplate simpMessagingTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.modelMapper = modelMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @Override
    public ChatRoomDTO createChatRoom(CreateRoomRequest request, Principal principal) {
        User customer = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //Tạo phòng
        ChatRoom newRoom = new ChatRoom();
        newRoom.setName(request.getName());
        newRoom.setStatus(ChatRoomStatus.PENDING); // Trạng thái: Đang chờ
        newRoom.getMembers().add(customer);

        ChatRoom savedRoom = chatRoomRepository.save(newRoom);
        ChatRoomDTO roomDTO = modelMapper.map(savedRoom, ChatRoomDTO.class);

        //Thông báo cho các staff
        try {
            log.info("Broadcasting new room {} to {}", savedRoom.getId(), STAFF_LOBBY_TOPIC);
            simpMessagingTemplate.convertAndSend(STAFF_LOBBY_TOPIC, roomDTO);
        } catch (Exception e) {
            log.error("--- BROADCAST FAILED --- {}", e.getMessage(), e);
        }
        return roomDTO;
    }

    @Override
    public ChatRoomDTO joinChatRoom(Long roomId, Principal principal) {
        User staff = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_CHAT_NOT_FOUND));

        //Phòng đã được nhận
        if (room.getStatus() != ChatRoomStatus.PENDING) {
            throw new AppException(ErrorCode.ROOM_INVALID);
        }

        //Thêm staff vào và đổi trạng thái sang active
        room.getMembers().add(staff);
        room.setStatus(ChatRoomStatus.ACTIVE);
        ChatRoom savedRoom = chatRoomRepository.save(room);


        Map<String, Object> updateMsg = Map.of(
                "type", "CLAIMED",
                "roomId", savedRoom.getId(),
                "staffName", staff.getFullName() != null ? staff.getFullName() : staff.getUsername()
        );
        String roomTopic = "/topic/chat-room/" + savedRoom.getId();
        Map<String, Object> roomUpdateMsg = Map.of(
                "type", "STAFF_JOINED", // (Client sẽ lắng nghe "type" này)
                "roomId", savedRoom.getId(),
                "staffName", staff.getFullName() != null ? staff.getFullName() : staff.getUsername()
        );
        simpMessagingTemplate.convertAndSend(STAFF_LOBBY_TOPIC, updateMsg);

        return modelMapper.map(savedRoom, ChatRoomDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ChatRoomDTO> getMyRooms(Principal principal) {
        User currentUser =  userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return currentUser.getChatRooms().stream()
                .map(room -> modelMapper.map(room, ChatRoomDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomMessageDTO> getChatHistory(Long roomId, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(()->new AppException(ErrorCode.ROOM_CHAT_NOT_FOUND));

        if(!room.getMembers().contains(currentUser)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        return messages.stream()
                .map(msg -> {
                    RoomMessageDTO dto = modelMapper.map(msg, RoomMessageDTO.class);
                    dto.setSenderId(msg.getSender().getId());
                    dto.setSenderName(msg.getSender().getFullName() != null ? msg.getSender().getFullName() : msg.getSender().getUsername());
                    dto.setRoomId(msg.getRoom().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDTO> getPendingRooms() {
        log.info("Fetching all PENDING chat rooms for Staff Lobby");

        // 1. Tìm tất cả phòng có status là PENDING
        List<ChatRoom> pendingRooms = chatRoomRepository.findByStatus(ChatRoomStatus.PENDING);

        // 2. Chuyển đổi (map) sang DTO
        return pendingRooms.stream()
                .map(room -> modelMapper.map(room, ChatRoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoomDTO closeChatRoom(Long roomId, Principal principal) {
        log.info("User {} attempt to close room {}", principal.getName(), roomId);

        User currentUser = getUserFromPrincipal(principal);
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_CHAT_NOT_FOUND));

        // Bảo mật: Chỉ thành viên mới được đóng
        if (!room.getMembers().contains(currentUser)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Nếu đã đóng rồi thì thôi
        if (room.getStatus() == ChatRoomStatus.CLOSED) {
            log.warn("Room {} is already CLOSED.", roomId);
            return modelMapper.map(room, ChatRoomDTO.class);
        }

        // Đổi trạng thái
        room.setStatus(ChatRoomStatus.CLOSED);
        ChatRoom savedRoom = chatRoomRepository.save(room);
        log.info("Room {} has been CLOSED by user {}", roomId, currentUser.getUsername());

        // Gửi thông báo "Chat đã kết thúc" cho người kia
        String roomTopic = "/topic/chat-room/" + savedRoom.getId();
        Map<String, Object> endMessage = Map.of(
                "type", "CHAT_ENDED", // (Client sẽ lắng nghe "type" này)
                "roomId", savedRoom.getId(),
                "endedBy", currentUser.getFullName() != null ? currentUser.getFullName() : currentUser.getUsername()
        );

        try {
            simpMessagingTemplate.convertAndSend(roomTopic, endMessage);
        } catch (Exception e) {
            log.error("--- BROADCAST FAILED (END_CHAT) --- {}", e.getMessage(), e);
        }

        return modelMapper.map(savedRoom, ChatRoomDTO.class);
    }

    private User getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }


}
