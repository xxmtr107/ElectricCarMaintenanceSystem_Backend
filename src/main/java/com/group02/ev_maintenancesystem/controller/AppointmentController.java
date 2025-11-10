package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemApproveRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemUpgradeRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.dto.response.AppointmentServiceItemDetailResponse;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AppointmentController {

    AppointmentService appointmentService;

    @PostMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<AppointmentResponse> createAppointment(
            @RequestBody @Valid CustomerAppointmentRequest appointment, Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment created successfully")
                .result(appointmentService.createAppointmentByCustomer(authentication, appointment))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')") // Yêu cầu 2
    public ApiResponse<List<AppointmentResponse>> getAllAppointments(Authentication authentication) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("All appointments fetched successfully")
                .result(appointmentService.getAll(authentication)).build();
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<AppointmentResponse> getAppointmentById(@PathVariable Long appointmentId) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment fetched successfully")
                .result(appointmentService.getAppointmentByAppointmentId(appointmentId))
                .build();
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or #customerId == authentication.principal.claims['userId']")
    public ApiResponse<List<AppointmentResponse>> getAppointmentsByCustomerId(@PathVariable Long customerId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully")
                .result(appointmentService.getAppointmentByCustomerId(customerId))
                .build();
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<AppointmentResponse>> getAppointmentsByVehicleId(@PathVariable Long vehicleId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully")
                .result(appointmentService.getAppointmentByVehicleId(vehicleId))
                .build();
    }

    @GetMapping("/technician/{technicianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or (hasRole('TECHNICIAN') and #technicianId == authentication.principal.claims['userId'])")
    public ApiResponse<List<AppointmentResponse>> getAppointmentsByTechnicianId(@PathVariable Long technicianId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully")
                .result(appointmentService.getAppointmentByTechnicianId(technicianId))
                .build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')") // Yêu cầu 2
    public ApiResponse<List<AppointmentResponse>> getAppointmentsByStatus(
            @PathVariable AppointmentStatus status, Authentication authentication) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully by status")
                .result(appointmentService.getAppointmentByStatus(status, authentication))
                .build();
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')") // Yêu cầu 2
    public ApiResponse<List<AppointmentResponse>> getAppointmentsBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate,
            Authentication authentication) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully for date range")
                .result(appointmentService.getAppointmentsBetweenDates(startDate, endDate, authentication))
                .build();
    }

    @PutMapping("/{appointmentId}/assign/{technicianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<AppointmentResponse> assignTechnician(
            @PathVariable Long appointmentId,
            @PathVariable Long technicianId,
            Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Technician assigned successfully")
                .result(appointmentService.assignTechnician(appointmentId, technicianId, authentication))
                .build();
    }

    @PutMapping("/setStatus/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')")
    public ApiResponse<AppointmentResponse> updateStatus(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentStatus newStatus,
            Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment status updated successfully")
                .result(appointmentService.setStatusAppointment(appointmentId, newStatus, authentication))
                .build();
    }

    @PutMapping("/cancel/{appointmentId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<AppointmentResponse> cancel(@PathVariable Long appointmentId, Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment cancelled successfully")
                .result(appointmentService.cancelAppointment(appointmentId,authentication))
                .build();
    }

    // --- ENDPOINTS MỚI CHO YÊU CẦU 1 (Check -> Replace) ---

    @PutMapping("/{appointmentId}/items/upgrade")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ApiResponse<AppointmentResponse> upgradeServiceItem(
            @PathVariable Long appointmentId,
            @Valid @RequestBody List<ServiceItemUpgradeRequest> requests, // <-- THAY ĐỔI Ở ĐÂY
            Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Service item upgrade(s) requested successfully. Waiting for approval.") // (Sửa message)
                .result(appointmentService.upgradeServiceItem(appointmentId, requests, authentication)) // <-- THAY ĐỔI Ở ĐÂY
                .build();
    }

    @PutMapping("/{appointmentId}/items/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<AppointmentResponse> approveServiceItem(
            @PathVariable Long appointmentId,
            @Valid @RequestBody List<ServiceItemApproveRequest> requests, // <-- THAY ĐỔI Ở ĐÂY
            Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Service item approval status(es) updated successfully.") // (Sửa message)
                .result(appointmentService.approveServiceItem(appointmentId, requests, authentication)) // <-- THAY ĐỔI Ở ĐÂY
                .build();
    }
    @GetMapping("/{appointmentId}/details")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<List<AppointmentServiceItemDetailResponse>> getAppointmentDetails(
            @PathVariable Long appointmentId, Authentication authentication) {
        return ApiResponse.<List<AppointmentServiceItemDetailResponse>>builder()
                .message("Appointment details fetched successfully")
                .result(appointmentService.getAppointmentDetails(appointmentId, authentication))
                .build();
    }
}