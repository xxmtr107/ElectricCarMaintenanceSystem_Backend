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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelPackageItemServiceImpl implements ModelPackageItemService {

    ModelPackageItemRepository modelPackageItemRepository;
    VehicleModelRepository vehicleModelRepository;
    ServicePackageRepository servicePackageRepository;
    ServiceItemRepository serviceItemRepository;
    ModelPackageItemMapper modelPackageItemMapper;

    @Override
    @Transactional
    public ModelPackageItemResponse createModelPackageItem(ModelPackageItemRequest request) {
        // Validate vehicle model
        VehicleModel vehicleModel = vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));

        // Validate service item
        ServiceItem serviceItem = serviceItemRepository.findById(request.getServiceItemId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

        ServicePackage servicePackage = null;
        if (request.getServicePackageId() != null) {
            servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));

            // Kiểm tra trùng lặp
            if (modelPackageItemRepository.existsByVehicleModelIdAndServicePackageIdAndServiceItemId(
                    request.getVehicleModelId(), request.getServicePackageId(), request.getServiceItemId())) {
                throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
            }
        } else {
            // Kiểm tra trùng lặp cho dịch vụ lẻ
            if (modelPackageItemRepository.existsByVehicleModelIdAndServicePackageIsNullAndServiceItemId(
                    request.getVehicleModelId(), request.getServiceItemId())) {
                throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_EXISTED);
            }
        }

        ModelPackageItem modelPackageItem = modelPackageItemMapper.toModelPackageItem(request);
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServicePackage(servicePackage);
        modelPackageItem.setServiceItem(serviceItem);

        return modelPackageItemMapper.toModelPackageItemResponse(
                modelPackageItemRepository.save(modelPackageItem));
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

    @Override
    public List<ModelPackageItemResponse> getByServicePackageId(Long servicePackageId) {
        if (!servicePackageRepository.existsById(servicePackageId)) {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }

        return modelPackageItemRepository.findByServicePackageId(servicePackageId).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelPackageItemResponse> getByVehicleModelAndPackage(Long vehicleModelId, Long servicePackageId) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }

        if (!servicePackageRepository.existsById(servicePackageId)) {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }

        return modelPackageItemRepository
                .findByVehicleModelIdAndServicePackageId(vehicleModelId, servicePackageId).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelPackageItemResponse> getIndividualServicesByModel(Long vehicleModelId) {
        if (!vehicleModelRepository.existsById(vehicleModelId)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }

        return modelPackageItemRepository.findByVehicleModelIdAndServicePackageIsNull(vehicleModelId).stream()
                .map(modelPackageItemMapper::toModelPackageItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ModelPackageItemResponse updateModelPackageItem(Long id, ModelPackageItemRequest request) {
        ModelPackageItem modelPackageItem = modelPackageItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND));

        // Validate vehicle model
        VehicleModel vehicleModel = vehicleModelRepository.findById(request.getVehicleModelId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND));

        // Validate service item
        ServiceItem serviceItem = serviceItemRepository.findById(request.getServiceItemId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

        ServicePackage servicePackage = null;
        if (request.getServicePackageId() != null) {
            servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        }

        modelPackageItemMapper.updateModelPackageItem(request, modelPackageItem);
        modelPackageItem.setVehicleModel(vehicleModel);
        modelPackageItem.setServicePackage(servicePackage);
        modelPackageItem.setServiceItem(serviceItem);

        return modelPackageItemMapper.toModelPackageItemResponse(
                modelPackageItemRepository.save(modelPackageItem));
    }

    @Override
    @Transactional
    public void deleteModelPackageItem(Long id) {
        if (!modelPackageItemRepository.existsById(id)) {
            throw new AppException(ErrorCode.MODEL_PACKAGE_ITEM_NOT_FOUND);
        }

        modelPackageItemRepository.deleteById(id);
    }
}

