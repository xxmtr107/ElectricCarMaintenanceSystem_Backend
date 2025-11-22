package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mails")
@PreAuthorize("hasRole('ADMIN')")
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    /**
     * Kích hoạt thủ công tác vụ gửi email nhắc nhở bảo dưỡng theo KM.
     */
    @PostMapping("/trigger/maintenance-reminder")
    public ApiResponse<List<String>> sendReminderMaintenance() {
        // 1. Gọi service một lần duy nhất và lấy kết quả
        List<String> sentEmails = emailService.reminderMaintenance();

        if (sentEmails.isEmpty()) {
            return ApiResponse.<List<String>>builder()
                    .message("No KM reminder emails to send.")
                    .result(sentEmails)
                    .build();
        } else {
            return ApiResponse.<List<String>>builder()
                    .message("KM reminder emails sent successfully.")
                    .result(sentEmails) // 2. Sử dụng kết quả đã lấy, KHÔNG gọi lại hàm service
                    .build();
        }
    }

    /**
     * Kích hoạt thủ công tác vụ gửi email nhắc nhở lịch hẹn (cho ngày mai).
     */
    @PostMapping("/trigger/schedule-reminder")
    public ApiResponse<List<String>> sendReminderSchedule() {
        List<String> sentEmails = emailService.upcomingAppointment();

        if (sentEmails.isEmpty()) {
            return ApiResponse.<List<String>>builder()
                    .message("No schedule reminder emails to send.")
                    .result(sentEmails)
                    .build();
        } else {
            return ApiResponse.<List<String>>builder()
                    .message("Schedule reminder emails sent successfully.")
                    .result(sentEmails)
                    .build();
        }
    }

    /**
     * Kích hoạt thủ công tác vụ gửi email nhắc nhở thanh toán.
     */
    @PostMapping("/trigger/payment-reminder")
    public ApiResponse<List<String>> sendReminderPayment() {
        List<String> sentEmails = emailService.remindPayment();

        if (sentEmails.isEmpty()) {
            return ApiResponse.<List<String>>builder()
                    .message("No payment reminder emails to send.")
                    .result(sentEmails)
                    .build();
        } else {
            return ApiResponse.<List<String>>builder()
                    .message("Payment reminder emails sent successfully.")
                    .result(sentEmails)
                    .build();
        }
    }
}