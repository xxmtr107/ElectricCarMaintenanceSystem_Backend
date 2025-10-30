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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional; // Thêm import
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelPackageItemServiceImpl implements ModelPackageItemService {

    ModelPackageItemRepository modelPackageItemRepository;
    VehicleModelRepository vehicleModelRepository;
    // Bỏ ServicePackageRepository nếu không còn dùng ở đây
    // ServicePackageRepository servicePackageRepository;
    ServiceItemRepository serviceItemRepository;
    ModelPackageItemMapper modelPackageItemMapper;

    // --- THÊM PHƯƠNG THỨC MỚI VÀO REPO (Đã làm ở bước 3d) ---
    // Cần Repository có: findByVehicleModelIdAndMilestoneKmAndServiceItemId
    // Cần Repository có: existsByVehicleModelIdAndMilestoneKmAndServiceItemId

    // --- Cập nhật phương thức kiểm tra trùng lặp ---
    private boolean checkDuplicate(Long modelId, Integer milestoneKm, Long itemId) {
        return modelPackageItemRepository.existsByVehicleModelIdAndMilestoneKmAndServiceItemId(modelId, milestoneKm, itemId);
    }

    // --- THÊM PHƯƠNG THỨC existsBy... vào ModelPackageItemRepository ---
    /* Trong ModelPackageItemRepository.java:
    boolean existsByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long itemId);
    Optional<ModelPackageItem> findByVehicleModelIdAndMilestoneKmAndServiceItemId(Long modelId, Integer milestoneKm, Long itemId); // Thêm cái này nữa
    */


    @Override
    @Transactional
    public ModelPackageItemResponse createModelPackageItem(ModelPackageItemRequest request) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));
        ServiceItem serviceItem = serviceItemRepository.findById(request.getServiceItemId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

        // Kiểm tra trùng lặp dựa trên model, milestone và item
        if (checkDuplicate(request.getVehicleModelId(), request.getMilestoneKm(), request.getServiceItemId())) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
        }

        ModelPackageItem modelPackageItem = modelPackageItemMapper.toModelPackageItem(request);
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServiceItem(serviceItem);
        // Không set servicePackage nữa
        // modelPackageItem.setMilestoneKm(request.getMilestoneKm()); // Mapper đã làm
        // modelPackageItem.setActionType(request.getActionType()); // Mapper đã làm
        // modelPackageItem.setPrice(request.getPrice()); // Mapper đã làm

        ModelPackageItem savedItem = modelPackageItemRepository.save(modelPackageItem);
        return modelPackageItemMapper.toModelPackageItemResponse(savedItem);
    }

    @Override
    public ModelPackageItemResponse getModelPackageItemById(Long id) {
        ModelPackageItem modelPackageItem = modelPackageItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND));
        return modelPackageItemMapper.toModelPackageItemResponse(modelPackageItem);
    }

    @Override
    public List<ModelPackageItemResponse> getAllModelPackageItems() {
        return modelPackageItemRepository.findAll().stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelPackageItemResponse> getByVehicleModelId(Long vehicleModelId) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        return modelPackageItemRepository.findByVehicleModelId(vehicleModelId).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    // --- Phương thức này không còn ý nghĩa nhiều, có thể bỏ hoặc sửa ---
    // @Override
    // public List<ModelPackageItemResponse> getByServicePackageId(Long servicePackageId) { ... }

    // --- Phương thức này đổi thành getByVehicleModelAndMilestoneKm ---
    // @Override
    // public List<ModelPackageItemResponse> getByVehicleModelAndPackage(Long vehicleModelId, Long servicePackageId) { ... }
    @Override
    public List<ModelPackageItemResponse> getByVehicleModelAndMilestoneKm(Long vehicleModelId, Integer milestoneKm) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        // Có thể kiểm tra milestoneKm hợp lệ nếu cần

        return modelPackageItemRepository
                .findByVehicleModelIdAndMilestoneKm(vehicleModelId, milestoneKm).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }


    // --- Phương thức này có thể không cần nữa, hoặc logic khác ---
    // @Override
    // public List<ModelPackageItemResponse> getIndividualServicesByModel(Long vehicleModelId) { ... }

    @Override
    @Transactional
    public ModelPackageItemResponse updateModelPackageItem(Long id, ModelPackageItemRequest request) {
        ModelPackageItem modelPackageItem = modelPackageItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND));

        VehicleModel vehicleModel = vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));
        ServiceItem serviceItem = serviceItemRepository.findById(request.getServiceItemId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

        // Kiểm tra trùng lặp nếu có sự thay đổi về model, milestone, item
        boolean changedKey = !modelPackageItem.getVehicleModel().getId().equals(request.getVehicleModelId()) ||
                !modelPackageItem.getMilestoneKm().equals(request.getMilestoneKm()) ||
                !modelPackageItem.getServiceItem().getId().equals(request.getServiceItemId());

        if (changedKey && checkDuplicate(request.getVehicleModelId(), request.getMilestoneKm(), request.getServiceItemId())) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
        }

        // Sử dụng mapper để cập nhật các trường từ request
        modelPackageItemMapper.updateModelPackageItem(modelPackageItem, request); // Gọi hàm update của mapper

        // Set lại các đối tượng liên kết (vì mapper ignore)
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServiceItem(serviceItem);
        // Không set servicePackage

        ModelPackageItem updatedItem = modelPackageItemRepository.save(modelPackageItem);
        return modelPackageItemMapper.toModelPackageItemResponse(updatedItem);
    }

    @Override
    @Transactional
    public void deleteModelPackageItem(Long id) {
        if (!modelPackageItemRepository.existsById(id)) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND);
        }
        modelPackageItemRepository.deleteById(id);
    }

    // --- Phương thức này đổi thành getMilestoneTotalPrice ---
    // @Override
    // public BigDecimal getPackageTotalPrice(Long vehicleModelId, Long servicePackageId) { ... }
    @Override
    public BigDecimal getMilestoneTotalPrice(Long vehicleModelId, Integer milestoneKm) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        // Kiểm tra milestoneKm hợp lệ

        List<ModelPackageItem> items = modelPackageItemRepository
                .findByVehicleModelIdAndMilestoneKm(vehicleModelId, milestoneKm);

        return items.stream()
                .map(ModelPackageItem::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // --- THÊM PHƯƠNG THỨC MỚI VÀO INTERFACE ModelPackageItemService ---
    /* Trong ModelPackageItemService.java:
    List<ModelPackageItemResponse> getByVehicleModelAndMilestoneKm(Long vehicleModelId, Integer milestoneKm);
    BigDecimal getMilestoneTotalPrice(Long vehicleModelId, Integer milestoneKm);
    // Bỏ các hàm liên quan đến servicePackageId, individual service nếu không dùng
    */
}