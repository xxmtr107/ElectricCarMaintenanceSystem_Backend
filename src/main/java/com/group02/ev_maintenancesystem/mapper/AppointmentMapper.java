package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import org.mapstruct.*;


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
    @Mapping(target = "technicianSpecialization", source = "technicianUser.specialization")

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleLicensePlate", source = "vehicle.licensePlate")
    @Mapping(target = "vehicleModel", source = "vehicle.model.name")
    AppointmentResponse toAppointmentResponse(Appointment appointment);

//    List<AppointmentResponse> toAppointmentResponse(List<Appointment> appointments);
}


