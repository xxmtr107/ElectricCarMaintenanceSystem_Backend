package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.InventoryResponse;
import com.group02.ev_maintenancesystem.entity.Inventory;
import java.util.List;

public interface InventoryService {
    // API chính: Cập nhật tồn kho (Nhập/Xuất)
    InventoryResponse updateStock(Long sparePartId, StockUpdateRequest request);

    // API chính: Xem kho của một trung tâm
    List<InventoryResponse> getInventoryByCenter(Long centerId);

    // Helper: Trừ kho (Gọi từ MaintenanceRecordService)
    void deductStock(Long centerId, Long sparePartId, int quantity);

    void deleteInventory(Long inventoryId);
}