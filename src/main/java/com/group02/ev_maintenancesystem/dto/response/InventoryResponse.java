package com.group02.ev_maintenancesystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResponse {
    Long id;
    Long serviceCenterId;
    String serviceCenterName;

    // Thông tin SparePart nhúng vào (hoặc tách ra tùy frontend)
    Long sparePartId;
    String partNumber;
    String partName;

    Boolean active;
    Integer quantity;
    Integer minStock;
}