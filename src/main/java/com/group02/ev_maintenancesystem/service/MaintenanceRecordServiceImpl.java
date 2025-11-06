package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.dto.request.PartUsageRequest;
import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void createMaintenanceRecord(Appointment appointment) {
        if (appointment == null) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            log.warn("Skipping MaintenanceRecord creation for appointment {}", appointment.getId());
            return;
        }
        if (maintenanceRecordRepository.findByAppointment_Id(appointment.getId()) != null) {
            log.warn("MaintenanceRecord already exists for appointment {}", appointment.getId());
            return;
        }
        if (appointment.getVehicle() == null) {
            throw new AppException(ErrorCode.VEHICLE_NOT_FOUND);
        }

        MaintenanceRecord maintenanceRecord = MaintenanceRecord.builder()
                .odometer(appointment.getVehicle().getCurrentKm())
                .appointment(appointment)
                .performedAt(LocalDateTime.now())
                .build();

        maintenanceRecordRepository.save(maintenanceRecord);
        log.info("Created MaintenanceRecord {} for Appointment ID {}", maintenanceRecord.getId(), appointment.getId());

        // --- LOGIC TỰ ĐỘNG TRỪ KHO (MỚI) ---
        autoDeductIncludedParts(appointment);
        // --- KẾT THÚC LOGIC TRỪ KHO ---
    }

    /**
     * Helper tự động trừ kho các phụ tùng đi kèm trong dịch vụ trọn gói
     */
    private void autoDeductIncludedParts(Appointment appointment) {
        log.info("Auto-deducting parts for appointment {}", appointment.getId());
        // Lấy các dịch vụ đã được duyệt (gói gốc + gói nâng cấp đã duyệt)
        List<AppointmentServiceItemDetail> approvedDetails = appointment.getServiceDetails().stream()
                .filter(AppointmentServiceItemDetail::getCustomerApproved)
                .toList();

        for (AppointmentServiceItemDetail detail : approvedDetails) {
            // Tìm định nghĩa ModelPackageItem tương ứng
            ModelPackageItem itemDefinition = modelPackageItemRepository
                    .findByVehicleModelIdAndMilestoneKmAndServiceItemId(
                            appointment.getVehicle().getModel().getId(),
                            appointment.getMilestoneKm(),
                            detail.getServiceItem().getId()
                    )
                    // Chỉ lấy nếu action type khớp (ví dụ: chỉ trừ kho cho REPLACE, không trừ cho CHECK)
                    .filter(def -> def.getActionType() == detail.getActionType())
                    .orElse(null);

            if (itemDefinition != null && itemDefinition.getIncludedSparePart() != null) {
                // Dịch vụ này có định nghĩa 1 phụ tùng đi kèm
                SparePart partToDeduct = itemDefinition.getIncludedSparePart();
                int quantityToDeduct = itemDefinition.getIncludedQuantity();

                if (partToDeduct == null) {
                    log.warn("Service item {} definition is missing 'includedSparePart' link.", detail.getServiceItem().getName());
                    continue;
                }

                log.info("Deducting {}x {} for service {}",
                        quantityToDeduct, partToDeduct.getPartNumber(), detail.getServiceItem().getName());

                // 1. Trừ kho
                StockUpdateRequest stockRequest = StockUpdateRequest.builder()
                        .changeQuantity(-quantityToDeduct)
                        .reason("Included in service for Appt ID " + appointment.getId())
                        .build();

                try {
                    sparePartService.updateStock(partToDeduct.getId(), stockRequest);

                    // 2. (Tùy chọn) Ghi PartUsage với giá 0 để lưu vết
                    // Bỏ qua, vì PartUsage chỉ dành cho PHÁT SINH NGOÀI

                } catch (AppException e) {
                    // Lỗi nghiêm trọng: Không đủ hàng trong kho
                    log.error("CRITICAL: Failed to auto-deduct part {} for appointment {}. Error: {}",
                            partToDeduct.getPartNumber(), appointment.getId(), e.getMessage());
                    // TODO: Xử lý nghiệp vụ (ví dụ: báo lỗi cho Staff)
                }
            }
        }
    }


    @Override
    public MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(MaintenanceRecordId).
                orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));
        return this.mapRecordToResponse(maintenanceRecord);
    }

    @Override
    public List<MaintenanceRecordResponse> getAll(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<MaintenanceRecord> maintenanceRecordList;

        if (user.isAdmin()) {
            maintenanceRecordList = maintenanceRecordRepository.findAll();
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            maintenanceRecordList = maintenanceRecordRepository.findByAppointment_ServiceCenter_Id(centerId);
        } else {
            maintenanceRecordList = Collections.emptyList();
        }
        return maintenanceRecordList.stream().map(this::mapRecordToResponse).toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByCustomerId(long customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAppointment_CustomerUser_Id(customerId);
        return records.stream()
                .map(this::mapRecordToResponse)
                .toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findMaintenanceRecordsByVehicleId(long vehicleId) {
        vehicleRepository.findById(vehicleId).
                orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<MaintenanceRecord> VehicleList = maintenanceRecordRepository.findByAppointment_Vehicle_Id(vehicleId);
        return VehicleList.stream().
                map(this::mapRecordToResponse).
                toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByTechnicianUserId(long technicianId) {
        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MaintenanceRecord> TechnicianList = maintenanceRecordRepository.findByAppointment_TechnicianUser_Id(technicianId);
        return TechnicianList.stream().
                map(this::mapRecordToResponse).
                toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end, Authentication authentication) {
        if (start.isAfter(end)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        User user = getAuthenticatedUser(authentication);
        List<MaintenanceRecord> maintenanceRecordList;
        if (user.isAdmin()) {
            maintenanceRecordList = maintenanceRecordRepository.findByAppointment_AppointmentDateBetween(start, end);
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            maintenanceRecordList = maintenanceRecordRepository.findByAppointment_AppointmentDateBetweenAndAppointment_ServiceCenter_Id(start, end, centerId);
        } else {
            maintenanceRecordList = Collections.emptyList();
        }
        return maintenanceRecordList.stream().
                map(this::mapRecordToResponse).
                toList();
    }

    @Override
    @Transactional
    public PartUsageResponse addPartToRecord(Long recordId, PartUsageRequest request, Authentication authentication) {
        User technician = getAuthenticatedUser(authentication);
        MaintenanceRecord record = maintenanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));

        if (record.getAppointment() == null || record.getAppointment().getTechnicianUser() == null ||
                !record.getAppointment().getTechnicianUser().getId().equals(technician.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        SparePart sparePart = sparePartRepository.findById(request.getSparePartId())
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

        int quantityNeeded = request.getQuantityUsed();
        if (sparePart.getQuantityInStock() < quantityNeeded) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Trừ kho
        StockUpdateRequest stockRequest = StockUpdateRequest.builder()
                .changeQuantity(-quantityNeeded)
                .reason("Used in maintenance record " + recordId + " (Extra part)")
                .build();
        sparePartService.updateStock(sparePart.getId(), stockRequest);

        // Tạo PartUsage (với logic tính tiền GỐC)
        PartUsage partUsage = PartUsage.builder()
                .maintenanceRecord(record)
                .sparePart(sparePart)
                .quantityUsed(quantityNeeded)
                // KHÔNG SET "isIncludedInService" (vì nó là false)
                .build();

        partUsage.caculateTotalPrice(); // Tính giá (sẽ là giá vật tư * số lượng)
        PartUsage savedPartUsage = partUsageRepository.save(partUsage);

        return partUsageMapper.toPartUsageResponse(savedPartUsage);
    }

    private MaintenanceRecordResponse mapRecordToResponse(MaintenanceRecord record) {
        if (record == null) {
            return null;
        }
        MaintenanceRecordResponse response = maintenanceRecordMapper.toMaintenanceRecordResponse(record);
        Appointment appointment = record.getAppointment();
        if (appointment == null) {
            return response;
        }

        // Map Dịch vụ trọn gói (từ Appointment)
        List<ModelPackageItemDTO> itemDTOs = new ArrayList<>();
        List<AppointmentServiceItemDetail> serviceDetails = appointment.getServiceDetails();
        if (serviceDetails != null) {
            for (AppointmentServiceItemDetail detail : serviceDetails) {
                if (detail.getCustomerApproved()) {
                    ServiceItem item = detail.getServiceItem();
                    if (item == null) continue;
                    ServiceItemDTO nestedItemDTO = ServiceItemDTO.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .build();
                    ModelPackageItemDTO outerItemDTO = new ModelPackageItemDTO(
                            nestedItemDTO,
                            detail.getPrice(),
                            detail.getActionType()
                    );
                    itemDTOs.add(outerItemDTO);
                }
            }
        }
        response.setServiceItems(itemDTOs);

        // Map Phụ tùng phát sinh (từ Record)
        List<PartUsageResponse> partUsageResponses = record.getPartUsages().stream()
                .map(partUsageMapper::toPartUsageResponse)
                .collect(Collectors.toList());
        response.setPartUsages(partUsageResponses);

        return response;
    }
}