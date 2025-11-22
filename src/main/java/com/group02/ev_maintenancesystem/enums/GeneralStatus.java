package com.group02.ev_maintenancesystem.enums;

public enum GeneralStatus {
    ACTIVE,   // Đang hoạt động / Hiển thị bình thường
    INACTIVE, // Tạm ngưng / Tạm khóa (vẫn có thể kích hoạt lại)
    DELETED   // Đã xóa (Soft delete - ẩn khỏi hệ thống nhưng vẫn lưu trong DB)
}