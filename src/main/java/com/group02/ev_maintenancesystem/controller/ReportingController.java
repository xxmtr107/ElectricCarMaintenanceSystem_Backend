package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.FinancialReportDTO;
import com.group02.ev_maintenancesystem.dto.PartUsageReportDTO;
import com.group02.ev_maintenancesystem.dto.ServiceUsageDTO;
import com.group02.ev_maintenancesystem.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor

public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/financial")
    public ApiResponse<FinancialReportDTO> getFinancialReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {

        return ApiResponse.<FinancialReportDTO>builder()
                .message("Financial report generated successfully")
                .result(reportingService.getFinancialReport(startDate, endDate, authentication))
                .build();
    }

    @GetMapping("/top-services")
    public ApiResponse<List<ServiceUsageDTO>> getTopUsedServices(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {

        return ApiResponse.<List<ServiceUsageDTO>>builder()
                .message("Top " + limit + " used services report generated successfully")
                .result(reportingService.getTopUsedServices(startDate, endDate, authentication, limit))
                .build();
    }

    @GetMapping("/top-parts")
    public ApiResponse<List<PartUsageReportDTO>> getTopUsedSpareParts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {

        return ApiResponse.<List<PartUsageReportDTO>>builder()
                .message("Top " + limit + " used spare parts report generated successfully")
                .result(reportingService.getTopUsedSpareParts(startDate, endDate, authentication, limit))
                .build();
    }
}