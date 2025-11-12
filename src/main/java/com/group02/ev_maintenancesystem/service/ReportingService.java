package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.FinancialReportDTO;
import com.group02.ev_maintenancesystem.dto.PartUsageReportDTO;
import com.group02.ev_maintenancesystem.dto.ServiceUsageDTO;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportingService {

    FinancialReportDTO getFinancialReport(LocalDateTime start, LocalDateTime end, Authentication authentication);

    List<ServiceUsageDTO> getTopUsedServices(LocalDateTime start, LocalDateTime end, Authentication authentication, int limit);

    List<PartUsageReportDTO> getTopUsedSpareParts(LocalDateTime start, LocalDateTime end, Authentication authentication, int limit);
}