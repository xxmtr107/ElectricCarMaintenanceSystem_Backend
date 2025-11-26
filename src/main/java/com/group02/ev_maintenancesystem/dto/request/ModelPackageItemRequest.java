package com.group02.ev_maintenancesystem.dto.request;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelPackageItemRequest {

    @NotNull(message = "NOT_BLANK")
    Long vehicleModelId;

    @NotNull(message = "NOT_BLANK")
    Integer milestoneKm;

    Integer milestoneMonth;


    Long serviceItemId;


    @DecimalMin(value = "0.01", message = "PRICE_INVALID")
    BigDecimal price;


    MaintenanceActionType actionType; // Thêm trường actionType

    Long includedSparePartId; // Thêm ID phụ tùng (có thể null)

    Integer includedQuantity; // Thêm số lượng (nên đặt giá trị mặc định)
}