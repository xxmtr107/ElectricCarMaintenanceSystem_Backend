package com.group02.ev_maintenancesystem.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoomRequest {
    // Tên của phòng chat (ví dụ: "Hỗ trợ đơn hẹn #123")
    private String name;

}
