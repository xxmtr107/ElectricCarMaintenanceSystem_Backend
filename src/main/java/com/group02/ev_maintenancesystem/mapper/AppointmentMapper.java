package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import com.group02.ev_maintenancesystem.entity.ServiceItem;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentRegistrationRequest request);
    Appointment toAppointmentCustomer(CustomerAppointmentRequest request);
    void updateAppointment(AppointmentUpdateRequest request, @MappingTarget Appointment appointment);

    @Mapping(target = "customerId", source = "customerUser.id")
    @Mapping(target = "customerName", source = "customerUser.fullName")
    @Mapping(target = "customerPhone", source = "customerUser.phone")
    @Mapping(target = "customerEmail", source = "customerUser.email")

    @Mapping(target = "technicianId", source = "technicianUser.id")
    @Mapping(target = "technicianName", source = "technicianUser.fullName")

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleLicensePlate", source = "vehicle.licensePlate")
    @Mapping(target = "vehicleModel", source = "vehicle.model.name")

    @Mapping(target = "servicePackageId", source = "servicePackage.id")
    @Mapping(target = "servicePackageName", source = "servicePackage.name")

    @Mapping(target = "nameCenter",source = "serviceCenter.name")
    @Mapping(target = "addressCenter",source = "serviceCenter.address")
    @Mapping(target = "districtCenter",source = "serviceCenter.district")
    @Mapping(target = "serviceItems", ignore = true)
    AppointmentResponse toAppointmentResponse(Appointment appointment);

    @AfterMapping
    default void mapServiceItems(Appointment appointment, @MappingTarget AppointmentResponse response,
                                  @Context ModelPackageItemRepository modelPackageItemRepository) {
        if (appointment.getServiceItems() == null || appointment.getServiceItems().isEmpty()) {
            response.setServiceItems(Collections.emptyList());
            return;
        }

        Long modelId = appointment.getVehicle().getModel().getId();
        Long packageId = appointment.getServicePackage() != null ?
                        appointment.getServicePackage().getId() : null;

        List<AppointmentResponse.ServiceItemDTO> dtoList = new ArrayList<>();

        for (ServiceItem item : appointment.getServiceItems()) {
            BigDecimal price = null; // Giá mặc định

            // Tìm giá theo model
            if (packageId != null) {
                // Tìm dịch vụ trong gói
                Optional<ModelPackageItem> modelItem = modelPackageItemRepository
                    .findByVehicleModelIdAndServicePackageIdAndServiceItemId(
                        modelId, packageId, item.getId());
                if (modelItem.isPresent()) {
                    price = modelItem.get().getPrice();
                }
            } else {
                // Tìm dịch vụ lẻ
                Optional<ModelPackageItem> modelItem = modelPackageItemRepository
                    .findByVehicleModelIdAndServicePackageIsNullAndServiceItemId(
                        modelId, item.getId());
                if (modelItem.isPresent()) {
                    price = modelItem.get().getPrice();
                }
            }

            AppointmentResponse.ServiceItemDTO dto = AppointmentResponse.ServiceItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(price)
                .build();

            dtoList.add(dto);
        }

        response.setServiceItems(dtoList);
    }
}
