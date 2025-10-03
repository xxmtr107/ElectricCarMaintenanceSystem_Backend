package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/create")
    public ApiResponse<AppointmentResponse> createAppointment(@RequestBody AppointmentRegistrationRequest appointment) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment created successfully")
                .result(appointmentService.createAppointment(appointment))
                .build();
    }

    @GetMapping("/getCustomerById/{customerId}")
    public ApiResponse<List<AppointmentResponse>> getByCustomer(@PathVariable long customerId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully")
                .result(appointmentService.getAppointmentByCustomerId(customerId))
                .build();
    }

    @GetMapping("/getById/{appointmentId}")
    public ApiResponse<AppointmentResponse> getById(@PathVariable long appointmentId) {
        Optional<AppointmentResponse> appointment = appointmentService.getAppointmentByAppointmentId(appointmentId);
        return ApiResponse.<AppointmentResponse>builder()
                .message(appointment.isPresent() ? "Appointment fetched successfully" : "Appointment not found")
                .result(appointment.orElse(null))
                .build();
    }

    @GetMapping("/getByStatus/{status}")
    public ApiResponse<List<AppointmentResponse>> getByStatus(@PathVariable AppointmentStatus status) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully by status")
                .result(appointmentService.getAppointmentByStatus(status))
                .build();
    }

    @GetMapping("/getByTechnicianIdAndAppointmentDate/{technicianId}/{date}")
    public ApiResponse<List<AppointmentResponse>> getByTechnicianAndSchedule(
            @PathVariable long technicianId,
            @PathVariable String date) {

        LocalDateTime scheduleDate;
        try {
            if (date.length() == 7) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                YearMonth ym = YearMonth.parse(date, formatter);
                scheduleDate = ym.atDay(1).atStartOfDay();
            } else if (date.length() == 10) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate d = LocalDate.parse(date, formatter);
                scheduleDate = d.atStartOfDay();
            } else if (date.length() == 13) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
                scheduleDate = LocalDateTime.parse(date, formatter);
            } else if (date.length() == 16) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                scheduleDate = LocalDateTime.parse(date, formatter);
            } else {
                throw new RuntimeException("Invalid date format! Use 'yyyy-MM', 'yyyy-MM-dd', 'yyyy-MM-dd HH' or 'yyyy-MM-dd HH:mm'");
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_LOCALDATETIMEFORMAT);
        }

        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Appointments fetched successfully for technician and schedule")
                .result(appointmentService.getTechnicianByTechnicianIdAndSchedule(technicianId, scheduleDate))
                .build();
    }

    @PutMapping("/update/{appointmentId}")
    public ApiResponse<AppointmentResponse> update(@PathVariable long appointmentId,
                                                   @RequestBody AppointmentUpdateRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment updated successfully")
                .result(appointmentService.updateAppointment(appointmentId, request))
                .build();
    }


    @PutMapping("/{appointmentId}/assign/{technicianId}")
    public ApiResponse<AppointmentResponse> assignTechnician(@PathVariable long appointmentId,
                                                             @PathVariable long technicianId) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Technician assigned successfully")
                .result(appointmentService.assignTechnician(appointmentId, technicianId))
                .build();
    }

    @PutMapping("/cancel/{appointmentId}")
    public ApiResponse<AppointmentResponse> cancel(@PathVariable long appointmentId) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Appointment cancelled successfully")
                .result(appointmentService.cancelAppointment(appointmentId))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<AppointmentResponse>> getAll() {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("All appointments fetched successfully")
                .result(appointmentService.getAll())
                .build();
    }

    @GetMapping("/{technicianId}/{date}/available")
    public ApiResponse<Boolean> getAvailableAppointments(@PathVariable long technicianId,@PathVariable String date) {
        LocalDateTime scheduleDate;
        try {
            if (date.length() == 7) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                YearMonth ym = YearMonth.parse(date, formatter);
                scheduleDate = ym.atDay(1).atStartOfDay();
            } else if (date.length() == 10) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate d = LocalDate.parse(date, formatter);
                scheduleDate = d.atStartOfDay();
            } else if (date.length() == 13) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
                scheduleDate = LocalDateTime.parse(date, formatter);
            } else if (date.length() == 16) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                scheduleDate = LocalDateTime.parse(date, formatter);
            } else {
                throw new RuntimeException("Invalid date format! Use 'yyyy-MM', 'yyyy-MM-dd', 'yyyy-MM-dd HH' or 'yyyy-MM-dd HH:mm'");
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_LOCALDATETIMEFORMAT);
        }
        boolean available=appointmentService.isTechnicianAvailable(technicianId,scheduleDate);
        return ApiResponse.<Boolean>builder()
                .message("Technician available")
                .result(available)
                .build();
    }
}
