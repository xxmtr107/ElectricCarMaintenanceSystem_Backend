package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentRegistrationRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "appointmentDate", source = "appointmentDate")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "notes", source = "notes")
    void updateAppointment(AppointmentUpdateRequest request, @MappingTarget Appointment appointment);

    @Mapping(target = "customerId", source = "customerUser.id")
    @Mapping(target = "technicianId", source = "technicianUser.id")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    AppointmentResponse toAppointmentResponse(Appointment appointment);

    List<AppointmentResponse> toAppointmentResponse(List<Appointment> appointments);
}


