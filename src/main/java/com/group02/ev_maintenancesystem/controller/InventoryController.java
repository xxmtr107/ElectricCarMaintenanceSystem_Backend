package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.InventoryResponse;
import com.group02.ev_maintenancesystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // API: Cập nhật kho (Nhập/Xuất) cho 1 phụ tùng tại 1 trung tâm
    // POST /inventories/spare-part/{id}/stock
    @PostMapping("/spare-part/{sparePartId}/stock")
    public ApiResponse<InventoryResponse> updateStock(
            @PathVariable Long sparePartId,
            @RequestBody StockUpdateRequest request) {
        return ApiResponse.<InventoryResponse>builder()
                .message("Inventory updated successfully")
                .result(inventoryService.updateStock(sparePartId, request))
                .build();
    }

    // API: Xem tồn kho của một trung tâm
    @GetMapping("/center/{centerId}")
    public ApiResponse<List<InventoryResponse>> getByCenter(@PathVariable Long centerId) {
        return ApiResponse.<List<InventoryResponse>>builder()
                .message("Inventory fetched successfully")
                .result(inventoryService.getInventoryByCenter(centerId))
                .build();
    }

    // API xóa kho (Chỉ xóa khi hết hàng)
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // Bật cái này nếu cần phân quyền
    public ApiResponse<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ApiResponse.<Void>builder()
                .message("Inventory record deleted successfully")
                .build();
    }
}