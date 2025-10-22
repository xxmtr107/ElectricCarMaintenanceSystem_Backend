package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.MaintenanceRecordMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    MaintenanceRecordRepository maintenanceRecordRepository;
    UserRepository userRepository;
    AppointmentRepository appointmentRepository;
    MaintenanceRecordMapper maintenanceRecordMapper;
    VehicleRepository vehicleRepository;
    ServicePackageRepository servicePackageRepository;
    ServiceItemRepository serviceItemRepository;

    @Override
    public List<MaintenanceRecordResponse> createMaintenanceRecord(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long customerId = jwt.getClaim("userId");

        List<Appointment> appointments = appointmentRepository.findByCustomerUserId(customerId);
        List<MaintenanceRecordResponse> responses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if ((appointment.getStatus().equals(AppointmentStatus.COMPLETED) ||
                    appointment.getStatus().equals(AppointmentStatus.CANCELLED)) &&
                    maintenanceRecordRepository.findByAppointment_Id(appointment.getId()) == null) {

                MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
                maintenanceRecord.setOdometer(appointment.getVehicle().getCurrentKm());
                maintenanceRecord.setAppointment(appointment);
                maintenanceRecord.setServicePackage(appointment.getServicePackage());
                maintenanceRecord.setTechnicianUser(appointment.getTechnicianUser());
                maintenanceRecord.setVehicle(appointment.getVehicle());
//                List<ServiceItem> items = serviceItemRepository.findByServicePackages_Id(appointment.getServicePackage().getId());
//                maintenanceRecord.setServiceItems(items);
                maintenanceRecord.setPerformedAt(appointment.getAppointmentDate());
                maintenanceRecord = maintenanceRecordRepository.save(maintenanceRecord);
                responses.add(maintenanceRecordMapper.toMaintenanceRecordResponse(maintenanceRecord));
            }
        }
        return responses;
    }

    @Override
    public MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(MaintenanceRecordId).
                orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));
        return maintenanceRecordMapper.toMaintenanceRecordResponse(maintenanceRecord);
    }

    @Override
    public List<MaintenanceRecordResponse> getAll() {
        List<MaintenanceRecord> MaintenanceRecordList = maintenanceRecordRepository.findAll();
        return MaintenanceRecordList.stream().
                map(maintenanceRecordMapper::toMaintenanceRecordResponse).
                toList();
    }

    @Override
    public MaintenanceRecordResponse findByCustomerId(long customerId) {
        userRepository.findById(customerId).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        MaintenanceRecord record = maintenanceRecordRepository.findByAppointment_CustomerUser_Id(customerId);
        if (record == null) {
            throw new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND);
        }

        return maintenanceRecordMapper.toMaintenanceRecordResponse(record);
    }

    @Override
    public List<MaintenanceRecordResponse> findMaintenanceRecordsByVehicleId(long vehicleId) {
        vehicleRepository.findById(vehicleId).
                orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<MaintenanceRecord> VehicleList = maintenanceRecordRepository.findByVehicle_Id(vehicleId);

        return VehicleList.stream().
                map(maintenanceRecordMapper::toMaintenanceRecordResponse).
                toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByTechnicianUserId(long technicianId) {

        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MaintenanceRecord> TechnicianList = maintenanceRecordRepository.findByAppointment_TechnicianUser_Id(technicianId);
        return TechnicianList.stream().
                map(maintenanceRecordMapper::toMaintenanceRecordResponse).
                toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        List<MaintenanceRecord> MaintenanceRecordList = maintenanceRecordRepository.findByAppointment_AppointmentDateBetween(start, end);
        return MaintenanceRecordList.stream().
                map(maintenanceRecordMapper::toMaintenanceRecordResponse).
                toList();
    }

    @Override
    @Transactional
    public void delete(long maintenanceRecordId) {
        maintenanceRecordRepository.findById(maintenanceRecordId)
                .orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));

        maintenanceRecordRepository.deleteByRecordId(maintenanceRecordId);
        maintenanceRecordRepository.flush();
    }

}
