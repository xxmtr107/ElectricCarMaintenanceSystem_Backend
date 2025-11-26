package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ModelPackageItemRequest;
import com.group02.ev_maintenancesystem.dto.response.ModelPackageItemResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.ModelPackageItemMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelPackageItemServiceImpl implements ModelPackageItemService {

    ModelPackageItemRepository modelPackageItemRepository;
    VehicleModelRepository vehicleModelRepository;
    SparePartRepository sparePartRepository;
    ServiceItemRepository serviceItemRepository;
    ModelPackageItemMapper modelPackageItemMapper;

    /**
     * Helper: Kiểm tra trùng lặp cấu hình
     * Một dòng xe, tại một mốc Km, không được có 2 dòng cấu hình cho cùng 1 dịch vụ.
     */
    private boolean checkDuplicate(Long modelId, Integer milestoneKm, Long itemId) {
        return modelPackageItemRepository.existsByVehicleModelIdAndMilestoneKmAndServiceItemId(modelId, milestoneKm, itemId);
    }

    @Override
    @Transactional
    public ModelPackageItemResponse createModelPackageItem(ModelPackageItemRequest request) {
        // 1. Validate Vehicle Model
        VehicleModel vehicleModel = vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));

        // Check Active cho Model [MỚI]
        if (!Boolean.TRUE.equals(vehicleModel.getActive())) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_INACTIVE);
        }

        // 2. Validate Service Item
        ServiceItem serviceItem = null;
        if (request.getServiceItemId() != null) {
            serviceItem = serviceItemRepository.findById(request.getServiceItemId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

            // MOVE THE CHECK INSIDE THIS BLOCK
            if (!Boolean.TRUE.equals(serviceItem.getActive())) {
                throw new AppException(ErrorCode.SERVICE_ITEM_INACTIVE);
            }
        }

        // 3. Kiểm tra trùng lặp
        if (checkDuplicate(request.getVehicleModelId(), request.getMilestoneKm(), request.getServiceItemId())) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
        }

        // 4. Map dữ liệu cơ bản
        ModelPackageItem modelPackageItem = modelPackageItemMapper.toModelPackageItem(request);
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServiceItem(serviceItem);
        // Map thêm trường tháng [MỚI]
        modelPackageItem.setMilestoneMonth(request.getMilestoneMonth());

        // 5. Xử lý Phụ tùng đi kèm (nếu có)
        if (request.getIncludedSparePartId() != null) {
            SparePart sparePart = sparePartRepository.findById(request.getIncludedSparePartId())
                    .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

            // Check Active cho SparePart [MỚI]
            if (!Boolean.TRUE.equals(sparePart.getActive())) {
                throw new AppException(ErrorCode.SPARE_PART_INACTIVE);
            }

            modelPackageItem.setIncludedSparePart(sparePart);

            // Default quantity là 1 nếu không nhập
            if (request.getIncludedQuantity() == null) {
                modelPackageItem.setIncludedQuantity(1);
            }
        } else {
            modelPackageItem.setIncludedSparePart(null);
            modelPackageItem.setIncludedQuantity(0);
        }

        ModelPackageItem savedItem = modelPackageItemRepository.save(modelPackageItem);
        return modelPackageItemMapper.toModelPackageItemResponse(savedItem);
    }

    @Override
    @Transactional
    public ModelPackageItemResponse updateModelPackageItem(Long id, ModelPackageItemRequest request) {
        ModelPackageItem modelPackageItem = modelPackageItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND));

        // 1. Validate Model & Service (nếu có thay đổi ID)
        VehicleModel vehicleModel = vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));
        if (!Boolean.TRUE.equals(vehicleModel.getActive())) throw new AppException(ErrorCode.VEHICLE_MODEL_INACTIVE);

        ServiceItem serviceItem = serviceItemRepository.findById(request.getServiceItemId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));
        if (!Boolean.TRUE.equals(serviceItem.getActive())) throw new AppException(ErrorCode.SERVICE_ITEM_INACTIVE);

        // 2. Kiểm tra trùng lặp nếu thay đổi key (Model + Km + Service)
        boolean isKeyChanged = !modelPackageItem.getVehicleModel().getId().equals(request.getVehicleModelId()) ||
                !modelPackageItem.getMilestoneKm().equals(request.getMilestoneKm()) ||
                !modelPackageItem.getServiceItem().getId().equals(request.getServiceItemId());

        if (isKeyChanged && checkDuplicate(request.getVehicleModelId(), request.getMilestoneKm(), request.getServiceItemId())) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
        }

        // 3. Cập nhật thông tin
        modelPackageItemMapper.updateModelPackageItem(modelPackageItem, request);
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServiceItem(serviceItem);
        modelPackageItem.setMilestoneMonth(request.getMilestoneMonth()); // [MỚI]

        // 4. Xử lý Phụ tùng
        if (request.getIncludedSparePartId() != null) {
            SparePart sparePart = sparePartRepository.findById(request.getIncludedSparePartId())
                    .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
            if (!Boolean.TRUE.equals(sparePart.getActive())) throw new AppException(ErrorCode.SPARE_PART_INACTIVE);

            modelPackageItem.setIncludedSparePart(sparePart);
            if (request.getIncludedQuantity() == null) {
                modelPackageItem.setIncludedQuantity(1);
            }
        } else {
            modelPackageItem.setIncludedSparePart(null);
            modelPackageItem.setIncludedQuantity(0);
        }

        ModelPackageItem updatedItem = modelPackageItemRepository.save(modelPackageItem);
        return modelPackageItemMapper.toModelPackageItemResponse(updatedItem);
    }

    @Override
    @Transactional
    public ModelPackageItemResponse updateStatus(Long id, Boolean isActive) {
        ModelPackageItem item = modelPackageItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND));

        item.setActive(isActive); // Hàm này có được nhờ kế thừa BaseEntity
        ModelPackageItem savedItem = modelPackageItemRepository.save(item);

        log.info("Updated status of ModelPackageItem ID {} to {}", id, isActive);
        return modelPackageItemMapper.toModelPackageItemResponse(savedItem);
    }

    @Override
    @Transactional
    public void deleteModelPackageItem(Long id) {
        if (!modelPackageItemRepository.existsById(id)) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND);
        }
        modelPackageItemRepository.deleteById(id);
    }

    @Override
    public ModelPackageItemResponse getModelPackageItemById(Long id) {
        ModelPackageItem modelPackageItem = modelPackageItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND));
        return modelPackageItemMapper.toModelPackageItemResponse(modelPackageItem);
    }

    @Override
    public List<ModelPackageItemResponse> getAllModelPackageItems() {
        // Admin xem tất cả, kể cả inactive
        return modelPackageItemRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelPackageItemResponse> getByVehicleModelId(Long vehicleModelId) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        return modelPackageItemRepository.findByVehicleModelIdAndMilestoneKmGreaterThanOrderByCreatedAtDesc(vehicleModelId, 1000).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelPackageItemResponse> getByVehicleModelAndMilestoneKm(Long vehicleModelId, Integer milestoneKm) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        return modelPackageItemRepository
                .findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(vehicleModelId, milestoneKm).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getMilestoneTotalPrice(Long vehicleModelId, Integer milestoneKm) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        // Chỉ tính tổng tiền của những mục đang ACTIVE
        List<ModelPackageItem> items = modelPackageItemRepository
                .findByVehicleModelIdAndMilestoneKmOrderByCreatedAtDesc(vehicleModelId, milestoneKm);

        return items.stream()
                .filter(item -> Boolean.TRUE.equals(item.getActive())) // [MỚI] Lọc Active
                .map(ModelPackageItem::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public void cloneConfiguration(Long sourceModelId, Long targetModelId) {
        log.info("Cloning configuration from model {} to model {}", sourceModelId, targetModelId);

        if (sourceModelId.equals(targetModelId)) {
            throw new AppException(ErrorCode.UNCATEGORIZED); // Không clone chính nó
        }

        VehicleModel sourceModel = vehicleModelRepository.findById(sourceModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));
        if (!Boolean.TRUE.equals(sourceModel.getActive())) throw new AppException(ErrorCode.VEHICLE_MODEL_INACTIVE);

        VehicleModel targetModel = vehicleModelRepository.findById(targetModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));
        if (!Boolean.TRUE.equals(targetModel.getActive())) throw new AppException(ErrorCode.VEHICLE_MODEL_INACTIVE);

        // Kiểm tra xem đích đã có cấu hình chưa
        List<ModelPackageItem> existingItems = modelPackageItemRepository.findByVehicleModelIdOrderByCreatedAtDesc(targetModelId);
        if (existingItems != null && !existingItems.isEmpty()) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED); // Đã có cấu hình, không ghi đè
        }

        // Lấy nguồn
        List<ModelPackageItem> sourceItems = modelPackageItemRepository.findByVehicleModelIdOrderByCreatedAtDesc(sourceModelId);
        if (sourceItems.isEmpty()) return;

        List<ModelPackageItem> newItems = new ArrayList<>();
        for (ModelPackageItem sourceItem : sourceItems) {
            // Chỉ clone những item đang Active
            if (!Boolean.TRUE.equals(sourceItem.getActive())) continue;

            ModelPackageItem newItem = ModelPackageItem.builder()
                    .vehicleModel(targetModel)
                    .milestoneKm(sourceItem.getMilestoneKm())
                    .milestoneMonth(sourceItem.getMilestoneMonth()) // [MỚI] Clone cả tháng
                    .serviceItem(sourceItem.getServiceItem())
                    .actionType(sourceItem.getActionType())
                    .price(sourceItem.getPrice())
                    .includedSparePart(sourceItem.getIncludedSparePart())
                    .includedQuantity(sourceItem.getIncludedQuantity())
                    .build();
            newItems.add(newItem);
        }

        modelPackageItemRepository.saveAll(newItems);
        log.info("Cloned {} items successfully.", newItems.size());
    }
}