package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.ModelPackageItemRequest;
import com.group02.ev_maintenancesystem.dto.response.ModelPackageItemResponse;
import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelPackageItemMapper {

    ModelPackageItem toModelPackageItem(ModelPackageItemRequest request);

    @Mapping(target = "vehicleModelId", source = "vehicleModel.id")
    @Mapping(target = "vehicleModelName", source = "vehicleModel.name")
    @Mapping(target = "servicePackageId", source = "servicePackage.id")
    @Mapping(target = "servicePackageName", source = "servicePackage.name")
    @Mapping(target = "serviceItemId", source = "serviceItem.id")
    @Mapping(target = "serviceItemName", source = "serviceItem.name")
    ModelPackageItemResponse toModelPackageItemResponse(ModelPackageItem modelPackageItem);

    void updateModelPackageItem(ModelPackageItemRequest request, @MappingTarget ModelPackageItem modelPackageItem);
}

