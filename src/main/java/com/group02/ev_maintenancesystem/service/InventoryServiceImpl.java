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
        SparePart part = sparePartRepository.findById(sparePartId)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
        ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));

        Inventory inventory = inventoryRepository
                .findByServiceCenterIdAndSparePartId(center.getId(), part.getId())
                .orElse(Inventory.builder()
                        .serviceCenter(center)
                        .sparePart(part)
                        .quantity(0)
                        .minStock(5) // Giá trị mặc định nếu tạo mới
                        .build());

        // 1. Cập nhật số lượng
        int newQty = inventory.getQuantity() + request.getChangeQuantity();
        if (newQty < 0) throw new AppException(ErrorCode.STOCK_QUANTITY_INVALID);
        inventory.setQuantity(newQty);

        // 2. Cập nhật minStock (nếu có trong request)
        if (request.getMinStock() != null) {
            inventory.setMinStock(request.getMinStock());
        }

        Inventory saved = inventoryRepository.save(inventory);

        return toResponse(saved);
    }

    @Override
    public List<InventoryResponse> getInventoryByCenter(Long centerId) {
        return inventoryRepository.findByServiceCenterId(centerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ... (Các hàm deductStock và createInitialInventory (dùng nội bộ) giữ nguyên nếu cần, hoặc xóa createInitialInventory đi nếu không còn ai gọi)

    @Override
    @Transactional
    public void deductStock(Long centerId, Long sparePartId, int quantity) {
        Inventory inv = inventoryRepository.findByServiceCenterIdAndSparePartId(centerId, sparePartId)
                .orElseThrow(() -> new AppException(ErrorCode.INSUFFICIENT_STOCK));

        if (inv.getQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }
        inv.setQuantity(inv.getQuantity() - quantity);
        inventoryRepository.save(inv);
    }

    // Hàm createInitialInventory có thể xóa nếu không còn dùng trong SparePartService
    // Nhưng để an toàn có thể giữ lại hoặc xóa đi tùy ý bạn.

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