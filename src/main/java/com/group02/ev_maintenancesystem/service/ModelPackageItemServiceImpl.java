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
import java.util.ArrayList;
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
    SparePartRepository sparePartRepository;
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
        return modelPackageItemRepository.findByVehicleModelIdAndMilestoneKmGreaterThan(vehicleModelId, 1000).stream()
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

        // Kiểm tra trùng lặp nếu có sự thay đổi về key
        boolean changedKey = !modelPackageItem.getVehicleModel().getId().equals(request.getVehicleModelId()) ||
                !modelPackageItem.getMilestoneKm().equals(request.getMilestoneKm()) ||
                !modelPackageItem.getServiceItem().getId().equals(request.getServiceItemId());

        if (changedKey && checkDuplicate(request.getVehicleModelId(), request.getMilestoneKm(), request.getServiceItemId())) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
        }

        // --- BẮT ĐẦU CẬP NHẬT LOGIC ---

        // 1. Sử dụng mapper để cập nhật các trường đơn giản (price, milestoneKm, actionType, includedQuantity)
        modelPackageItemMapper.updateModelPackageItem(modelPackageItem, request);

        // 2. Xử lý các entity liên kết (ServiceCenter và ServiceItem)
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServiceItem(serviceItem);

        // 3. Xử lý logic cho phụ tùng đi kèm (SparePart)
        if (request.getIncludedSparePartId() != null) {
            SparePart sparePart = sparePartRepository.findById(request.getIncludedSparePartId())
                    .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
            modelPackageItem.setIncludedSparePart(sparePart);

            // Nếu quantity không được cung cấp, đặt mặc định là 1 khi có phụ tùng
            if (request.getIncludedQuantity() == null) {
                modelPackageItem.setIncludedQuantity(1);
            }
        } else {
            // Nếu không có ID phụ tùng, set là null và số lượng là 0
            modelPackageItem.setIncludedSparePart(null);
            modelPackageItem.setIncludedQuantity(0);
        }

        // (Nếu request.getIncludedQuantity() != null, mapper đã set giá trị đó rồi)

        // --- KẾT THÚC CẬP NHẬT LOGIC ---

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

    @Override
    @Transactional
    public void cloneConfiguration(Long sourceModelId, Long targetModelId) {
        log.info("Bắt đầu yêu cầu sao chép cấu hình từ model {} sang model {}", sourceModelId, targetModelId);

        // 1. Validate
        if (sourceModelId.equals(targetModelId)) {
            throw new AppException(ErrorCode.CANNOT_DELETE_SERVICE_CENTER); // Tạm dùng lỗi này, nên tạo lỗi mới
        }

        VehicleModel sourceModel = vehicleModelRepository.findById(sourceModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));

        VehicleModel targetModel = vehicleModelRepository.findById(targetModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));

        // 2. Kiểm tra xem model đích đã có cấu hình chưa (RẤT QUAN TRỌNG)
        List<ModelPackageItem> existingItems = modelPackageItemRepository.findByVehicleModelId(targetModelId);
        if (existingItems != null && !existingItems.isEmpty()) {
            log.warn("Model đích {} đã có {} hạng mục. Không thể sao chép.", targetModelId, existingItems.size());
            // Bạn nên tạo một ErrorCode mới, ví dụ: MODEL_CONFIG_ALREADY_EXISTS
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
        }

        // 3. Lấy tất cả cấu hình của model nguồn
        List<ModelPackageItem> sourceItems = modelPackageItemRepository.findByVehicleModelId(sourceModelId);
        if (sourceItems == null || sourceItems.isEmpty()) {
            log.warn("Model nguồn {} không có cấu hình nào để sao chép.", sourceModelId);
            return; // Không có gì để làm
        }

        // 4. Tạo danh sách các bản sao mới
        List<ModelPackageItem> newItems = new ArrayList<>();
        for (ModelPackageItem sourceItem : sourceItems) {
            ModelPackageItem newItem = ModelPackageItem.builder()
                    .vehicleModel(targetModel) // Gán cho model ĐÍCH
                    .milestoneKm(sourceItem.getMilestoneKm())
                    .serviceItem(sourceItem.getServiceItem()) // Giữ nguyên tham chiếu ServiceItem
                    .actionType(sourceItem.getActionType())
                    .price(sourceItem.getPrice())
                    .includedSparePart(sourceItem.getIncludedSparePart()) // Giữ nguyên tham chiếu Phụ tùng
                    .includedQuantity(sourceItem.getIncludedQuantity())
                    .build();
            // (Lưu ý: BaseEntity (createdAt, createdBy...) sẽ tự động được gán)
            newItems.add(newItem);
        }

        // 5. Lưu tất cả bản sao vào database
        modelPackageItemRepository.saveAll(newItems);

        log.info("Sao chép thành công {} hạng mục từ model {} ({}) sang model {} ({})",
                newItems.size(), sourceModel.getName(), sourceModelId, targetModel.getName(), targetModelId);
    }
}