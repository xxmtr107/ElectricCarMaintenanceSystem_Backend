package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.dto.request.PartUsageRequest;
import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.dto.response.PartUsageResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.MaintenanceRecordMapper;
import com.group02.ev_maintenancesystem.mapper.PartUsageMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    PartUsageRepository partUsageRepository;
    SparePartRepository sparePartRepository;
    SparePartService sparePartService;
    PartUsageMapper partUsageMapper;
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
        maintenanceRecord.setPerformedAt(appointment.getAppointmentDate());
//        List<ModelPackageItem> modelItems = modelPackageItemRepository
//                .findByVehicleModelIdAndServicePackageId(appointment.getVehicle().getModel().getId(), appointment.getServicePackage().getId());
//        List<ServiceItem> packageItems = modelItems.stream()
//                .map(ModelPackageItem::getServiceItem)
//                .collect(Collectors.toList());
//        maintenanceRecord.setServiceItems(packageItems);
        maintenanceRecordRepository.save(maintenanceRecord);
    }


    @Override
    public MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(MaintenanceRecordId).
                orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));
        return this.mapRecordToResponse(maintenanceRecord); // <-- *** USE HELPER ***
    }

    @Override
    public List<MaintenanceRecordResponse> getAll() {
        List<MaintenanceRecord> MaintenanceRecordList = maintenanceRecordRepository.findAll();
        return MaintenanceRecordList.stream().
                map(this::mapRecordToResponse). // <-- *** USE HELPER ***
                        toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByCustomerId(long customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAppointment_CustomerUser_Id(customerId);

        return records.stream()
                .map(this::mapRecordToResponse) // <-- *** USE HELPER ***
                .toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findMaintenanceRecordsByVehicleId(long vehicleId) {
        vehicleRepository.findById(vehicleId).
                orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<MaintenanceRecord> VehicleList = maintenanceRecordRepository.findByAppointment_Vehicle_Id(vehicleId);

        return VehicleList.stream().
                map(this::mapRecordToResponse). // <-- *** USE HELPER ***
                        toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByTechnicianUserId(long technicianId) {

        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MaintenanceRecord> TechnicianList = maintenanceRecordRepository.findByAppointment_TechnicianUser_Id(technicianId);
        return TechnicianList.stream().
                map(this::mapRecordToResponse). // <-- *** USE HELPER ***
                        toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        List<MaintenanceRecord> MaintenanceRecordList = maintenanceRecordRepository.findByAppointment_AppointmentDateBetween(start, end);
        return MaintenanceRecordList.stream().
                map(this::mapRecordToResponse). // <-- *** USE HELPER ***
                        toList();
    }
    @Override
    @Transactional
    public PartUsageResponse addPartToRecord(Long recordId, PartUsageRequest request) {
        // 1. Tìm MaintenanceRecord
        MaintenanceRecord record = maintenanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));

        // 2. Tìm SparePart
        SparePart sparePart = sparePartRepository.findById(request.getSparePartId())
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

        // 3. Kiểm tra số lượng tồn kho
        int quantityNeeded = request.getQuantityUsed();
        if (sparePart.getQuantityInStock() < quantityNeeded) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // 4. Trừ kho (Tái sử dụng logic từ SparePartService)
        StockUpdateRequest stockRequest = StockUpdateRequest.builder()
                .changeQuantity(-quantityNeeded) // Trừ kho
                .reason("Used in maintenance record " + recordId)
                .build();
        sparePartService.updateStock(sparePart.getId(), stockRequest);

        // 5. Tạo và lưu PartUsage
        PartUsage partUsage = PartUsage.builder()
                .maintenanceRecord(record)
                .sparePart(sparePart)
                .quantityUsed(quantityNeeded)
                .build();

        // Giá sẽ được tự động tính bằng @PrePersist

        partUsage.caculateTotalPrice();
        PartUsage savedPartUsage = partUsageRepository.save(partUsage);

        // 6. Map và trả về response
        return partUsageMapper.toPartUsageResponse(savedPartUsage);
    }

    private MaintenanceRecordResponse mapRecordToResponse(MaintenanceRecord record) {
        if (record == null) {
            return null;
        }

        // Bước 1: Map cơ bản
        MaintenanceRecordResponse response = maintenanceRecordMapper.toMaintenanceRecordResponse(record);

        // Bước 2: Lấy thông tin
        Appointment appointment = record.getAppointment();
        if (appointment == null || appointment.getVehicle() == null || appointment.getVehicle().getModel() == null || appointment.getServiceItems() == null) {
            return response;
        }

        VehicleModel model = appointment.getVehicle().getModel();
        List<ServiceItem> serviceItems = appointment.getServiceItems();
        Integer milestoneKm = appointment.getMilestoneKm(); // <-- *** USE THE SAVED MILESTONE ***

        // SỬA LỖI: Xử lý dữ liệu cũ không có milestoneKm (Giống hệt AppointmentServiceImpl)
        if (milestoneKm == null) {
            log.warn("milestoneKm is null for record ID {} (Appointment ID {}). Attempting to infer from estimatedCost.", record.getId(), appointment.getId());

            List<Integer> possibleMilestones = modelPackageItemRepository
                    .findByVehicleModelId(model.getId()).stream()
                    .map(ModelPackageItem::getMilestoneKm)
                    .distinct()
                    .collect(Collectors.toList());

            for (Integer ms : possibleMilestones) {
                BigDecimal total = modelPackageItemRepository
                        .findByVehicleModelIdAndMilestoneKm(model.getId(), ms).stream()
                        .map(ModelPackageItem::getPrice)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (appointment.getEstimatedCost() != null && total.compareTo(appointment.getEstimatedCost()) == 0) {
                    milestoneKm = ms;
                    log.info("Inferred milestone {}km for record ID {}", ms, record.getId());
                    break;
                }
            }
        }
        // Kết thúc sửa lỗi

        if (milestoneKm == null) {
            log.warn("Could not determine milestoneKm for record ID {}, cannot determine item prices.", record.getId());
            return response;
        }

        // *** BƯỚC 4: LOGIC ĐÚNG THEO YÊU CẦU CỦA BẠN ***
        List<ModelPackageItemDTO> itemDTOs = new ArrayList<>();
        for (ServiceItem item : serviceItems) {
            Optional<ModelPackageItem> mpiOpt = modelPackageItemRepository
                    .findByVehicleModelIdAndMilestoneKmAndServiceItemId(model.getId(), milestoneKm, item.getId());

            BigDecimal price = mpiOpt.map(ModelPackageItem::getPrice).orElse(BigDecimal.ZERO);
            MaintenanceActionType actionType = mpiOpt.map(ModelPackageItem::getActionType).orElse(null);

            // 1. Tạo DTO lồng bên trong
            ServiceItemDTO nestedItemDTO = ServiceItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .build();

            // 2. Tạo DTO bên ngoài
            ModelPackageItemDTO outerItemDTO = new ModelPackageItemDTO(
                    nestedItemDTO,
                    price,
                    actionType
            );

            itemDTOs.add(outerItemDTO);
        }

        response.setServiceItems(itemDTOs); // Set danh sách DTO đã build
        // *** KẾT THÚC THAY ĐỔI ***

        return response;
    }
}
