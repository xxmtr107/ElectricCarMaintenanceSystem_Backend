package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.FinancialReportDTO;
import com.group02.ev_maintenancesystem.dto.MonthlyRevenueDTO;
import com.group02.ev_maintenancesystem.dto.PartUsageReportDTO;
import com.group02.ev_maintenancesystem.dto.ServiceUsageDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.thymeleaf.util.DateUtils.month;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {

    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final PartUsageRepository partUsageRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private List<Appointment> getCompletedAppointmentsInDateRange(LocalDateTime start, LocalDateTime end, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Appointment> appointments;

        if (user.isAdmin()) {
            appointments = appointmentRepository.findByAppointmentDateBetween(start, end);
        } else if (user.isStaff()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findByAppointmentDateBetweenAndServiceCenterId(start, end, centerId);
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    @Override
    public FinancialReportDTO getFinancialReport(LocalDateTime start, LocalDateTime end, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Long centerId = null;
        List<Appointment> completedAppointments = getCompletedAppointmentsInDateRange(start, end, authentication);

        if (user.isStaff()) {
            centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
        }

        List<Invoice> invoices = completedAppointments.stream()
                .map(a -> a.getMaintenanceRecord() != null ? a.getMaintenanceRecord().getInvoice() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Invoice> paidInvoices = invoices.stream()
                .filter(i -> "PAID".equalsIgnoreCase(i.getStatus())) // Lọc hóa đơn đã thanh toán
                .collect(Collectors.toList());

        BigDecimal totalRevenue = paidInvoices.stream()
                .map(Invoice::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return FinancialReportDTO.builder()
                .startDate(start)
                .endDate(end)
                .serviceCenterId(centerId)
                .totalCompletedAppointments((long) completedAppointments.size())
                .totalPaidInvoices((long) paidInvoices.size())
                .totalRevenue(totalRevenue)
                .build();
    }

    @Override
    public List<ServiceUsageDTO> getTopUsedServices(LocalDateTime start, LocalDateTime end, Authentication authentication, int limit) {
        List<Appointment> completedAppointments = getCompletedAppointmentsInDateRange(start, end, authentication);

        // Đếm số lần sử dụng của mỗi service item từ các hạng mục đã được duyệt
        Map<ServiceItem, Long> serviceCountMap = completedAppointments.stream()
                .flatMap(a -> a.getServiceDetails().stream())
                .filter(AppointmentServiceItemDetail::getCustomerApproved)
                .map(AppointmentServiceItemDetail::getServiceItem)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Chuyển Map thành List<ServiceUsageDTO> và sắp xếp
        return serviceCountMap.entrySet().stream()
                .map(entry -> new ServiceUsageDTO(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getValue()
                ))
                .sorted((dto1, dto2) -> dto2.getUsageCount().compareTo(dto1.getUsageCount())) // Sắp xếp giảm dần
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartUsageReportDTO> getTopUsedSpareParts(LocalDateTime start, LocalDateTime end, Authentication authentication, int limit) {
        List<Appointment> completedAppointments = getCompletedAppointmentsInDateRange(start, end, authentication);

        // Lấy tất cả MaintenanceRecord từ các lịch hẹn đã hoàn thành
        List<MaintenanceRecord> records = completedAppointments.stream()
                .map(Appointment::getMaintenanceRecord)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Đếm số lượng phụ tùng (chỉ tính phụ tùng phát sinh)
        Map<SparePart, Long> partCountMap = records.stream()
                .flatMap(r -> r.getPartUsages().stream())
                .collect(Collectors.groupingBy(
                        PartUsage::getSparePart,
                        Collectors.summingLong(PartUsage::getQuantityUsed)
                ));

        // Chuyển Map thành List<PartUsageReportDTO> và sắp xếp
        return partCountMap.entrySet().stream()
                .map(entry -> new PartUsageReportDTO(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getKey().getPartNumber(),
                        entry.getValue()
                ))
                .sorted((dto1, dto2) -> dto2.getTotalQuantityUsed().compareTo(dto1.getTotalQuantityUsed())) // Sắp xếp giảm dần
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyRevenueDTO> getMonthlyRevenueByYear(int year, Authentication authentication) {
        List<MonthlyRevenueDTO> result = new ArrayList<>();

        for(int month = 1; month <=12; month++){
            //Xác định ngày đầu tiên của tháng
            LocalDateTime startMonth = LocalDateTime.of(year, month, 1 , 0 , 0 ,0);

            //Xác địng ngày cuối cùng của tháng
            LocalDateTime endMonth = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

            FinancialReportDTO report = getFinancialReport(startMonth, endMonth, authentication);

            BigDecimal revenueMonth = report.getTotalRevenue();
            if(revenueMonth == null){
                revenueMonth = BigDecimal.ZERO;
            }

            result.add(MonthlyRevenueDTO.builder()
                    .month(month)
                    .totalRevenue(revenueMonth)
                    .build());
        }
        return result;
    }
}