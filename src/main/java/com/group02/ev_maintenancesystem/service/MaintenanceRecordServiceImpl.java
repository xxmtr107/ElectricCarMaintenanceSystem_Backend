package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.dto.request.PartUsageRequest;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.dto.response.PartUsageDetailResponse;
import com.group02.ev_maintenancesystem.dto.response.PartUsageResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import com.group02.ev_maintenancesystem.enums.Role;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.MaintenanceRecordMapper;
import com.group02.ev_maintenancesystem.mapper.PartUsageMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    MaintenanceRecordRepository maintenanceRecordRepository;
    UserRepository userRepository;
    MaintenanceRecordMapper maintenanceRecordMapper;
    VehicleRepository vehicleRepository;
    PartUsageRepository partUsageRepository;
    SparePartRepository sparePartRepository;
    PartUsageMapper partUsageMapper;
    ModelPackageItemRepository modelPackageItemRepository;
    ServiceCenterSparePartRepository inventoryRepository; // Inject Repository mới
    InventoryService inventoryService; // Inject Service mới


    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // [THÊM VALIDATE]
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }

        return user;
    }

    @Override
    @Transactional
    public void createMaintenanceRecord(Appointment appointment) {
        if (appointment == null) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return;
        }
        if (maintenanceRecordRepository.findByAppointment_Id(appointment.getId()) != null) {
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
        autoDeductIncludedParts(appointment);
    }

    private void autoDeductIncludedParts(Appointment appointment) {
        Long centerId = appointment.getServiceCenter().getId();

        List<AppointmentServiceItemDetail> approvedDetails = appointment.getServiceDetails().stream()
                .filter(AppointmentServiceItemDetail::getCustomerApproved)
                .toList();

        for (AppointmentServiceItemDetail detail : approvedDetails) {
            ModelPackageItem itemDefinition = null;
            Long modelId = appointment.getVehicle().getModel().getId();
            Long serviceItemId = detail.getServiceItem().getId();
            MaintenanceActionType actionType = detail.getActionType();

            if (actionType == MaintenanceActionType.REPLACE) {
                itemDefinition = modelPackageItemRepository
                        .findByVehicleModelIdAndMilestoneKmAndServiceItemId(
                                modelId, appointment.getMilestoneKm(), serviceItemId)
                        .filter(def -> def.getActionType() == MaintenanceActionType.REPLACE)
                        .orElse(null);

                if (itemDefinition == null) {
                    itemDefinition = modelPackageItemRepository
                            .findByVehicleModelIdAndMilestoneKmAndServiceItemId(modelId, 1, serviceItemId)
                            .filter(def -> def.getActionType() == MaintenanceActionType.REPLACE)
                            .orElse(null);
                }
            }

            if (itemDefinition != null && itemDefinition.getIncludedSparePart() != null) {
                SparePart part = itemDefinition.getIncludedSparePart();
                int quantityToDeduct = itemDefinition.getIncludedQuantity();

                // [MỚI] Gọi InventoryService để trừ kho
                // Hàm này sẽ tự check số lượng và throw Exception nếu thiếu
                inventoryService.deductStock(centerId, part.getId(), quantityToDeduct);
            }
        }
    }

    // ... (Giữ nguyên các hàm get/find khác) ...

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
            maintenanceRecordList = maintenanceRecordRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            maintenanceRecordList = maintenanceRecordRepository.findByAppointment_ServiceCenter_IdOrderByCreatedAtDesc(centerId);
        } else {
            maintenanceRecordList = Collections.emptyList();
        }
        return maintenanceRecordList.stream().map(this::mapRecordToResponse).toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByCustomerId(long customerId) {
        userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAppointment_CustomerUser_IdOrderByCreatedAtDesc(customerId);
        return records.stream().map(this::mapRecordToResponse).toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findMaintenanceRecordsByVehicleId(long vehicleId) {
        vehicleRepository.findById(vehicleId).orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<MaintenanceRecord> VehicleList = maintenanceRecordRepository.findByAppointment_Vehicle_IdOrderByCreatedAtDesc(vehicleId);
        return VehicleList.stream().map(this::mapRecordToResponse).toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByTechnicianUserId(long technicianId) {
        userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<MaintenanceRecord> TechnicianList = maintenanceRecordRepository.findByAppointment_TechnicianUser_IdOrderByCreatedAtDesc(technicianId);
        return TechnicianList.stream().map(this::mapRecordToResponse).toList();
    }

    @Override
    public List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end, Authentication authentication) {
        if (start.isAfter(end)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        User user = getAuthenticatedUser(authentication);
        List<MaintenanceRecord> maintenanceRecordList;
        if (user.isAdmin()) {
            maintenanceRecordList = maintenanceRecordRepository.findByAppointment_AppointmentDateBetweenOrderByCreatedAtDesc(start, end);
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            maintenanceRecordList = maintenanceRecordRepository.findByAppointment_AppointmentDateBetweenAndAppointment_ServiceCenter_IdOrderByCreatedAtDesc(start, end, centerId);
        } else {
            maintenanceRecordList = Collections.emptyList();
        }
        return maintenanceRecordList.stream().map(this::mapRecordToResponse).toList();
    }

    @Override
    @Transactional
    public PartUsageResponse addPartToRecord(Long recordId, PartUsageRequest request, Authentication authentication) {
        User technician = getAuthenticatedUser(authentication);
        MaintenanceRecord record = maintenanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));

        // Kiểm tra quyền và lấy Center ID
        if (record.getAppointment() == null || record.getAppointment().getServiceCenter() == null) {
            throw new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND);
        }
        Long centerId = record.getAppointment().getServiceCenter().getId();

        if (record.getAppointment().getTechnicianUser() == null ||
                !record.getAppointment().getTechnicianUser().getId().equals(technician.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        SparePart sparePart = sparePartRepository.findById(request.getSparePartId())
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
        // [THÊM VALIDATE]
        if (!Boolean.TRUE.equals(sparePart.getActive())) {
            throw new AppException(ErrorCode.SPARE_PART_INACTIVE);
        }
        // [MỚI] Gọi InventoryService để trừ kho
        int quantityNeeded = request.getQuantityUsed();
        inventoryService.deductStock(centerId, sparePart.getId(), quantityNeeded);

        // Tạo PartUsage
        PartUsage partUsage = PartUsage.builder()
                .maintenanceRecord(record)
                .sparePart(sparePart)
                .quantityUsed(quantityNeeded)
                .build();

        partUsage.caculateTotalPrice();
        PartUsage savedPartUsage = partUsageRepository.save(partUsage);

        return partUsageMapper.toPartUsageResponse(savedPartUsage);
    }

    // ... (Giữ nguyên mapRecordToResponse và getAllPartUsagesForRecord) ...

    private MaintenanceRecordResponse mapRecordToResponse(MaintenanceRecord record) {
        if (record == null) return null;
        MaintenanceRecordResponse response = maintenanceRecordMapper.toMaintenanceRecordResponse(record);
        Appointment appointment = record.getAppointment();
        if (appointment == null) return response;

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
        List<PartUsageResponse> partUsageResponses = record.getPartUsages().stream()
                .map(partUsageMapper::toPartUsageResponse)
                .collect(Collectors.toList());
        response.setPartUsages(partUsageResponses);
        return response;
    }

    @Override
    public List<PartUsageDetailResponse> getAllPartUsagesForRecord(Long recordId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        MaintenanceRecord record = maintenanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND));

        // (Logic checkRecordAccess giữ nguyên)
        if (!user.isAdmin()) {
            Appointment appt = record.getAppointment();
            if (appt == null) throw new AppException(ErrorCode.UNAUTHORIZED);
            boolean isCenterStaff = (user.isStaff() || user.isTechnician()) && user.getServiceCenter() != null
                    && appt.getServiceCenter() != null && user.getServiceCenter().getId().equals(appt.getServiceCenter().getId());
            boolean isOwner = user.isCustomer() && appt.getCustomerUser() != null && appt.getCustomerUser().getId().equals(user.getId());
            if (!isCenterStaff && !isOwner) throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<PartUsageDetailResponse> allParts = new ArrayList<>();
        // Extra parts
        List<PartUsageDetailResponse> extraParts = record.getPartUsages().stream()
                .map(pu -> PartUsageDetailResponse.builder()
                        .id(pu.getId())
                        .quantityUsed(pu.getQuantityUsed())
                        .totalPrice(pu.getTotalPrice())
                        .sparePartId(pu.getSparePart().getId())
                        .sparePartName(pu.getSparePart().getName())
                        .sparePartNumber(pu.getSparePart().getPartNumber())
                        .maintenanceRecordId(pu.getMaintenanceRecord().getId())
                        .createdAt(pu.getCreatedAt())
                        .usageType("EXTRA")
                        .build())
                .collect(Collectors.toList());
        allParts.addAll(extraParts);

        // Included parts (Logic giữ nguyên như cũ)
        Appointment appointment = record.getAppointment();
        if (appointment != null && appointment.getVehicle() != null && appointment.getVehicle().getModel() != null) {
            Long modelId = appointment.getVehicle().getModel().getId();
            List<AppointmentServiceItemDetail> approvedReplaces = appointment.getServiceDetails().stream()
                    .filter(d -> d.getCustomerApproved() && d.getActionType() == MaintenanceActionType.REPLACE)
                    .toList();

            for (AppointmentServiceItemDetail detail : approvedReplaces) {
                // (Logic tìm definition giữ nguyên)
                ModelPackageItem itemDefinition = modelPackageItemRepository
                        .findByVehicleModelIdAndMilestoneKmAndServiceItemId(modelId, appointment.getMilestoneKm(), detail.getServiceItem().getId())
                        .filter(def -> def.getActionType() == MaintenanceActionType.REPLACE)
                        .orElse(null);
                if (itemDefinition == null) {
                    itemDefinition = modelPackageItemRepository
                            .findByVehicleModelIdAndMilestoneKmAndServiceItemId(modelId, 1, detail.getServiceItem().getId())
                            .filter(def -> def.getActionType() == MaintenanceActionType.REPLACE)
                            .orElse(null);
                }

                if (itemDefinition != null && itemDefinition.getIncludedSparePart() != null) {
                    SparePart includedPart = itemDefinition.getIncludedSparePart();
                    PartUsageDetailResponse includedPartResponse = PartUsageDetailResponse.builder()
                            .id(null)
                            .quantityUsed(itemDefinition.getIncludedQuantity())
                            .totalPrice(BigDecimal.ZERO)
                            .sparePartId(includedPart.getId())
                            .sparePartName(includedPart.getName())
                            .sparePartNumber(includedPart.getPartNumber())
                            .maintenanceRecordId(recordId)
                            .createdAt(detail.getCreatedAt())
                            .usageType("INCLUDED")
                            .build();
                    allParts.add(includedPartResponse);
                }
            }
        }
        return allParts;
    }
}