package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    ModelPackageItemRepository modelPackageItemRepository;

    @Override
    @Transactional
    public void createMaintenanceRecord(Appointment appointment) {
        if (appointment == null) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }

        AppointmentStatus status = appointment.getStatus();
        if (status != AppointmentStatus.COMPLETED && status != AppointmentStatus.CANCELLED) {
            return; // Chỉ tạo record khi status là COMPLETED hoặc CANCELLED
        }

        // Kiểm tra xem đã có maintenance record cho appointment này chưa
        MaintenanceRecord existingRecord = maintenanceRecordRepository.findByAppointment_Id(appointment.getId());
        if (existingRecord != null) {
            return;
        }

        if (appointment.getVehicle() == null) {
            throw new AppException(ErrorCode.VEHICLE_NOT_FOUND);
        }

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setOdometer(appointment.getVehicle().getCurrentKm());
        maintenanceRecord.setAppointment(appointment);
        maintenanceRecord.setServicePackage(appointment.getServicePackage());
        maintenanceRecord.setTechnicianUser(appointment.getTechnicianUser());
        maintenanceRecord.setVehicle(appointment.getVehicle());
        maintenanceRecord.setPerformedAt(appointment.getAppointmentDate());
        maintenanceRecord.setServicePackage(appointment.getServicePackage());
        List<ModelPackageItem> modelItems = modelPackageItemRepository
                .findByVehicleModelIdAndServicePackageId(appointment.getVehicle().getModel().getId(), appointment.getServicePackage().getId());
        List<ServiceItem> packageItems = modelItems.stream()
                .map(ModelPackageItem::getServiceItem)
                .collect(Collectors.toList());
        maintenanceRecord.setServiceItems(packageItems);
        maintenanceRecordRepository.save(maintenanceRecord);
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
    public List<MaintenanceRecordResponse> findByCustomerId(long customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAppointment_CustomerUser_Id(customerId);

        return records.stream()
                .map(maintenanceRecordMapper::toMaintenanceRecordResponse)
                .toList();
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


}
