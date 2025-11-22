package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.dto.response.InvoiceResponse;
import com.group02.ev_maintenancesystem.mapper.InvoiceMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;
    private final MaintenanceRecordService maintenanceRecordService; // Dùng để map record lồng nhau

    @Override
    @Transactional
    public InvoiceResponse createInvoiceForAppointment(Long appointmentId, Authentication authentication) {
        // 1. Lấy thông tin Staff
        User staff = getAuthenticatedUser(authentication);
        if (!staff.isAdmin() && !staff.isStaff()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 2. Lấy Appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // 2a. Kiểm tra bảo mật Center
        if (staff.isStaff() && (appointment.getServiceCenter() == null ||
                !appointment.getServiceCenter().getId().equals(staff.getServiceCenter().getId()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 3. Chỉ tạo hoá đơn khi đã COMPLETED
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_COMPLETED);
        }

        // 4. Lấy Maintenance Record
        MaintenanceRecord record = maintenanceRecordRepository.findByAppointment_Id(appointmentId);
        if (record == null) {
            throw new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND);
        }

        // 5. Kiểm tra hoá đơn đã tồn tại chưa
        if (record.getInvoice() != null) {
            throw new AppException(ErrorCode.INVOICE_ALREADY_EXISTS);
        }

        // 6. Tính tổng tiền (THEO LOGIC MỚI)

        // 6a. Tiền dịch vụ trọn gói (từ các chi tiết đã được duyệt)
        BigDecimal serviceTotal = appointment.getServiceDetails().stream()
                .filter(AppointmentServiceItemDetail::getCustomerApproved) // Chỉ tính các mục đã được duyệt
                .map(AppointmentServiceItemDetail::getPrice)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 6b. Tiền phụ tùng (PartUsage)
        // Logic của PartUsage (đã sửa) sẽ tự động gán giá = 0 cho các phụ tùng "included"
        // Nên hàm SUM này CHỈ CỘNG CÁC PHỤ TÙNG THÊM NGOÀI
        BigDecimal partTotal = record.getPartUsages().stream()
                .map(PartUsage::getTotalPrice)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal finalTotal = serviceTotal.add(partTotal);

        // 7. Tạo hoá đơn
        Invoice invoice = Invoice.builder()
                .totalAmount(finalTotal)
                .status("UNPAID") // Trạng thái ban đầu
                .maintenanceRecord(record)
                .serviceCenter(appointment.getServiceCenter()) // Lấy center từ appointment
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Cập nhật liên kết 2 chiều
        record.setInvoice(savedInvoice);
        maintenanceRecordRepository.save(record);

        log.info("Invoice ID {} created for Appointment ID {}. ServiceTotal: {}, PartTotal: {}, FinalTotal: {}",
                savedInvoice.getId(), appointmentId, serviceTotal, partTotal, finalTotal);

        // 8. Trả về Response (Map đầy đủ thông tin)
        return getInvoiceById(savedInvoice.getId(), authentication);
    }

    @Override
    public InvoiceResponse getInvoiceByAppointmentId(Long appointmentId, Authentication authentication) {
        MaintenanceRecord record = maintenanceRecordRepository.findByAppointment_Id(appointmentId);
        if (record == null || record.getInvoice() == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_FOUND);
        }
        return getInvoiceById(record.getInvoice().getId(), authentication); // Gọi lại hàm getInvoiceById để dùng chung logic
    }

    @Override
    public InvoiceResponse getInvoiceById(Long invoiceId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        // Bảo mật: Kiểm tra quyền xem
        checkInvoiceAccess(user, invoice);

        // Map invoice cơ bản
        InvoiceResponse response = invoiceMapper.toInvoiceResponse(invoice);

        // Map MaintenanceRecord (vì mapper có thể không map sâu)
        if (invoice.getMaintenanceRecord() != null) {
            // Gọi hàm map của MaintenanceRecordService để lấy response đầy đủ
            response.setMaintenanceRecord(
                    maintenanceRecordService.getByMaintenanceRecordId(invoice.getMaintenanceRecord().getId())
            );
        }
        return response;
    }

    @Override
    public List<InvoiceResponse> getMyInvoices(Authentication authentication) {
        User customer = getAuthenticatedUser(authentication);
        if (!customer.isCustomer()) {
            return Collections.emptyList();
        }

        // Lấy các record của customer, rồi tìm invoice
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByAppointment_CustomerUser_IdOrderByCreatedAtDesc(customer.getId());

        return records.stream()
                .map(MaintenanceRecord::getInvoice)
                .filter(java.util.Objects::nonNull)
                .map(invoice -> getInvoiceById(invoice.getId(), authentication)) // Dùng lại logic map và bảo mật
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getAllInvoices(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Invoice> invoices;

        if (user.isAdmin()) {
            invoices = invoiceRepository.findAll();
        } else if (user.isStaff()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            // Cần tạo hàm findByServiceCenter_Id trong InvoiceRepository
            invoices = invoiceRepository.findByServiceCenterIdOrderByCreatedAtDesc(centerId);
        } else {
            return Collections.emptyList(); // Chỉ Admin/Staff được xem all
        }

        return invoices.stream()
                .map(invoice -> getInvoiceById(invoice.getId(), authentication)) // Dùng lại logic map
                .collect(Collectors.toList());
    }

    // --- Helper Methods ---

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkInvoiceAccess(User user, Invoice invoice) {
        if (user.isAdmin()) {
            return; // Admin được xem tất cả
        }

        if (user.isStaff()) {
            // Staff phải đúng center
            if (user.getServiceCenter() != null && invoice.getServiceCenter() != null &&
                    invoice.getServiceCenter().getId().equals(user.getServiceCenter().getId())) {
                return;
            }
        }

        if (user.isCustomer()) {
            // Customer phải là người sở hữu
            Long customerId = invoice.getMaintenanceRecord().getAppointment().getCustomerUser().getId();
            if (customerId.equals(user.getId())) {
                return;
            }
        }
        // Mặc định là không có quyền
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }
}