package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockUpdateRequest {
    @NotNull(message = "NOT_BLANK")
    Integer changeQuantity; // Số lượng thay đổi (+ hoặc -)

    String reason; // Lý do (Nhập hàng, Xuất hủy...)

    @NotNull(message = "SERVICE_CENTER_REQUIRED")
    Long serviceCenterId; // Nhập cho kho nào

    @Min(value = 0, message = "MIN_STOCK_INVALID")
    Integer minStock; // (Tùy chọn) Cập nhật mức cảnh báo tồn kho mới
}