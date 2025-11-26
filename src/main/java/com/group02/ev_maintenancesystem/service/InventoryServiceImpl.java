package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.InventoryResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.exception.*;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ServiceCenterRepository serviceCenterRepository;
    private final SparePartRepository sparePartRepository;

    @Override
    @Transactional
    public InventoryResponse updateStock(Long sparePartId, StockUpdateRequest request) {
        // 1. Tìm phụ tùng
        SparePart part = sparePartRepository.findById(sparePartId)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

        // [THÊM VALIDATE] Kiểm tra trạng thái hoạt động
        if (!Boolean.TRUE.equals(part.getActive())) {
            throw new AppException(ErrorCode.SPARE_PART_INACTIVE);
        }

        // 2. Tìm trung tâm
        ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));

        // (Có thể validate thêm Service Center có Active không nếu muốn chặt chẽ)
        if (!Boolean.TRUE.equals(center.getActive())) {
            throw new AppException(ErrorCode.SERVICE_CENTER_INACTIVE);
        }

        // 3. Tìm hoặc tạo mới kho hàng (Inventory)
        Inventory inventory = inventoryRepository
                .findByServiceCenterIdAndSparePartId(center.getId(), part.getId())
                .orElse(Inventory.builder()
                        .serviceCenter(center)
                        .sparePart(part)
                        .quantity(0)
                        .minStock(5) // Giá trị mặc định
                        .build());

        // 4. Cập nhật số lượng
        int newQty = inventory.getQuantity() + request.getChangeQuantity();
        if (newQty < 0) {
            throw new AppException(ErrorCode.STOCK_QUANTITY_INVALID);
        }
        inventory.setQuantity(newQty);

        // 5. Cập nhật mức tối thiểu (nếu có)
        if (request.getMinStock() != null) {
            inventory.setMinStock(request.getMinStock());
        }

        Inventory saved = inventoryRepository.save(inventory);

        return toResponse(saved);
    }

    @Override
    public List<InventoryResponse> getInventoryByCenter(Long centerId) {
        return inventoryRepository.findByServiceCenterId(centerId).stream()
                // Có thể lọc chỉ hiện các phụ tùng đang Active trong kho
                .filter(inv -> Boolean.TRUE.equals(inv.getSparePart().getActive()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deductStock(Long centerId, Long sparePartId, int quantity) {
        Inventory inv = inventoryRepository.findByServiceCenterIdAndSparePartId(centerId, sparePartId)
                .orElseThrow(() -> new AppException(ErrorCode.INSUFFICIENT_STOCK));

        // [THÊM VALIDATE] Khi trừ kho (sử dụng) cũng nên check active để đảm bảo không dùng hàng cấm
        if (!Boolean.TRUE.equals(inv.getSparePart().getActive())) {
            throw new AppException(ErrorCode.SPARE_PART_INACTIVE);
        }

        if (inv.getQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }
        inv.setQuantity(inv.getQuantity() - quantity);
        inventoryRepository.save(inv);
    }

    // ... (Các hàm khác giữ nguyên)

    private InventoryResponse toResponse(Inventory inv) {
        return InventoryResponse.builder()
                .id(inv.getId())
                .serviceCenterId(inv.getServiceCenter().getId())
                .serviceCenterName(inv.getServiceCenter().getName())
                .sparePartId(inv.getSparePart().getId())
                .partNumber(inv.getSparePart().getPartNumber())
                .partName(inv.getSparePart().getName())
                .quantity(inv.getQuantity())
                .minStock(inv.getMinStock())
                .build();
    }
}