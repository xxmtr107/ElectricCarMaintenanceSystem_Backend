package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.ModelPackageItemRequest;
import com.group02.ev_maintenancesystem.dto.response.ModelPackageItemResponse;
import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelPackageItemMapper {

    // --- Cần bỏ qua servicePackage khi map từ Request ---
    ModelPackageItem toModelPackageItem(ModelPackageItemRequest request);

    // --- Map milestoneKm và bỏ qua servicePackageId/Name trong Response ---
    @Mapping(target = "vehicleModelId", source = "vehicleModel.id")
    @Mapping(target = "vehicleModelName", source = "vehicleModel.name")
    // Bỏ map servicePackage
    // @Mapping(target = "servicePackageId", source = "servicePackage.id")
    // @Mapping(target = "servicePackageName", source = "servicePackage.name")
    @Mapping(target = "milestoneKm", source = "milestoneKm") // Đảm bảo map milestoneKm
    @Mapping(target = "serviceItemId", source = "serviceItem.id")
    @Mapping(target = "serviceItemName", source = "serviceItem.name")
    @Mapping(target = "actionType", source = "actionType")
    ModelPackageItemResponse toModelPackageItemResponse(ModelPackageItem modelPackageItem);

    void updateModelPackageItem(@MappingTarget ModelPackageItem modelPackageItem, ModelPackageItemRequest request); // Đảo vị trí tham số theo convention
}