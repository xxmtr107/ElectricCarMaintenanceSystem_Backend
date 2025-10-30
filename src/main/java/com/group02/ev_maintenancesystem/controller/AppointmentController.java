package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import com.group02.ev_maintenancesystem.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
    public ApiResponse<AppointmentResponse> createAppointment(@RequestBody @Valid CustomerAppointmentRequest appointment, Authentication authentication) {


        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment created successfully")
                .result(appointmentService.createAppointmentByCustomer(authentication, appointment))
                .build();
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<List<AppointmentResponse>> getAllAppointments() {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("All appointments fetched successfully")
                .result(appointmentService.getAll()).build();

    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<List<AppointmentResponse>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully by status")
                .result(appointmentService.getAppointmentByStatus(status))
                .build();
    }

    // Admin lấy appointments theo khoảng thời gian
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<List<AppointmentResponse>> getAppointmentsBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully for date range")
                .result(appointmentService.getAppointmentsBetweenDates(startDate, endDate))
                .build();
    }

    // Admin assign technician cho appointment
    @PutMapping("/{appointmentId}/assign/{technicianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<AppointmentResponse> assignTechnician(
            @PathVariable Long appointmentId,
            @PathVariable Long technicianId) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Technician assigned successfully")
                .result(appointmentService.assignTechnician(appointmentId, technicianId))
                .build();
    }

    @PutMapping("/setStatus/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<AppointmentResponse> update(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentStatus newStatus) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment updated successfully")
                .result(appointmentService.setStatusAppointment(appointmentId, newStatus))
                .build();
    }

    @PutMapping("/cancel/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF','CUSTOMER','TECHNICIAN')")
    public ApiResponse<AppointmentResponse> cancel(@PathVariable Long appointmentId, Authentication authentication) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment cancelled successfully")
                .result(appointmentService.cancelAppointment(appointmentId,authentication))
                .build();
    }

//    @GetMapping("/getByTechnicianIdAndAppointmentDate/{technicianId}/{date}")
//    public ApiResponse<List<AppointmentResponse>> getByTechnicianAndSchedule(
//            @PathVariable long technicianId,
//            @PathVariable String date) {

//        LocalDateTime scheduleDate;
//        try {
//            if (date.length() == 7) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
//                YearMonth ym = YearMonth.parse(date, formatter);
//                scheduleDate = ym.atDay(1).atStartOfDay();
//            } else if (date.length() == 10) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                LocalDate d = LocalDate.parse(date, formatter);
//                scheduleDate = d.atStartOfDay();
//            } else if (date.length() == 13) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
//                scheduleDate = LocalDateTime.parse(date, formatter);
//            } else if (date.length() == 16) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//                scheduleDate = LocalDateTime.parse(date, formatter);
//            } else {
//                throw new RuntimeException("Invalid date format! Use 'yyyy-MM', 'yyyy-MM-dd', 'yyyy-MM-dd HH' or 'yyyy-MM-dd HH:mm'");
//            }
//        } catch (Exception e) {
//            throw new AppException(ErrorCode.INVALID_LOCALDATETIMEFORMAT);
//        }
//
//        return ApiResponse.<List<AppointmentResponse>>builder()
//                .message("Appointments fetched successfully for technician and schedule")
//                .result(appointmentService.getTechnicianByTechnicianIdAndSchedule(technicianId, scheduleDate))
//                .build();
//        return null;
//    }
//
//    @PutMapping("/update/{appointmentId}")
//    public ApiResponse<AppointmentResponse> update(@PathVariable long appointmentId,
//                                                   @RequestBody AppointmentUpdateRequest request) {
//        return ApiResponse.<AppointmentResponse>builder()
//                .message("Appointment updated successfully")
//                .result(appointmentService.updateAppointment(appointmentId, request))
//                .build();
//    }
//
//
//    @PutMapping("/{appointmentId}/assign/{technicianId}")
//    public ApiResponse<AppointmentResponse> assignTechnician(@PathVariable long appointmentId,
//                                                             @PathVariable long technicianId) {
//        return ApiResponse.<AppointmentResponse>builder()
//                .message("Technician assigned successfully")
//                .result(appointmentService.assignTechnician(appointmentId, technicianId))
//                .build();
//    }
//
//    @PutMapping("/cancel/{appointmentId}")
//    public ApiResponse<AppointmentResponse> cancel(@PathVariable long appointmentId) {
//        return ApiResponse.<AppointmentResponse>builder()
//                .message("Appointment cancelled successfully")
//                .result(appointmentService.cancelAppointment(appointmentId))
//                .build();
//    }
//
//    @GetMapping("/getAll")
//    public ApiResponse<List<AppointmentResponse>> getAll() {
//        return ApiResponse.<List<AppointmentResponse>>builder()
//                .message("All appointments fetched successfully")
//                .result(appointmentService.getAll())
//                .build();
//    }
//
//    @GetMapping("/{technicianId}/{date}/available")
//    public ApiResponse<Boolean> getAvailableAppointments(@PathVariable long technicianId,@PathVariable String date) {
//        LocalDateTime scheduleDate;
//        try {
//            if (date.length() == 7) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
//                YearMonth ym = YearMonth.parse(date, formatter);
//                scheduleDate = ym.atDay(1).atStartOfDay();
//            } else if (date.length() == 10) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                LocalDate d = LocalDate.parse(date, formatter);
//                scheduleDate = d.atStartOfDay();
//            } else if (date.length() == 13) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
//                scheduleDate = LocalDateTime.parse(date, formatter);
//            } else if (date.length() == 16) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//                scheduleDate = LocalDateTime.parse(date, formatter);
//            } else {
//                throw new RuntimeException("Invalid date format! Use 'yyyy-MM', 'yyyy-MM-dd', 'yyyy-MM-dd HH' or 'yyyy-MM-dd HH:mm'");
//            }
//        } catch (Exception e) {
//            throw new AppException(ErrorCode.INVALID_LOCALDATETIMEFORMAT);
//        }
//        boolean available=appointmentService.isTechnicianAvailable(technicianId,scheduleDate);
//        return ApiResponse.<Boolean>builder()
//                .message("Technician available")
//                .result(available)
//                .build();
//        return null;
//    }
}
