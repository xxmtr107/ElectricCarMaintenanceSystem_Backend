package com.group02.ev_maintenancesystem.controller;


import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // Thêm PreAuthorize
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.util.List;

@RestController
@RequestMapping("/mails")
@PreAuthorize("hasRole('ADMIN')") // <-- THÊM: Chỉ Admin mới được kích hoạt mail thủ công
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    /**
     * Kích hoạt thủ công tác vụ gửi email nhắc nhở bảo dưỡng theo KM.
     * Tác vụ này cũng tự động chạy lúc 8h sáng hàng ngày.
     */
    @PostMapping("/trigger/maintenance-reminder") // <-- ĐỔI TÊN
    public ApiResponse<List<String> >sendReminderMaintenance() throws MessagingException {
        List<String> list=emailService.reminderMaintenance();
        try {
            if(list.isEmpty()){
                return ApiResponse.<List<String>>builder().
                        message("No KM reminder emails to send.").
                        result(list).
                        build();
            }else{
                return ApiResponse.<List<String>>builder().
                        message("KM reminder emails sent successfully.").
                        result(emailService.reminderMaintenance()).
                        build();
            }
        }catch (MessagingException e){
            return ApiResponse.<List<String>>builder().
                    message("Failed to send mail: " + e.getMessage()).
                    build();
        }
    }

    /**
     * Kích hoạt thủ công tác vụ gửi email nhắc nhở lịch hẹn (cho ngày mai).
     * Tác vụ này cũng tự động chạy lúc 8h sáng hàng ngày.
     */
    @PostMapping("/trigger/schedule-reminder") // <-- ĐỔI TÊN
    public ApiResponse<List<String>> sendReminderSchedule() throws MessagingException {
        List<String> list=emailService.upcomingAppointment();
        try {
            if(list.isEmpty()){
                return ApiResponse.<List<String>>builder().
                        message("No schedule reminder emails to send.").
                        result(list).
                        build();
            }else{
                return ApiResponse.<List<String>>builder().
                        message("Schedule reminder emails sent successfully.").
                        result(emailService.upcomingAppointment()).
                        build();
            }
        }catch (MessagingException e){
            return ApiResponse.<List<String>>builder().
                    message("Failed to send mail: " + e.getMessage()).
                    build();
        }
    }

    /**
     * Kích hoạt thủ công tác vụ gửi email nhắc nhở thanh toán (cho hóa đơn UNPAID/FAILED).
     * Tác vụ này cũng tự động chạy lúc 8h sáng hàng ngày.
     */
    @PostMapping("/trigger/payment-reminder") // <-- ĐỔI TÊN
    public ApiResponse<List<String>> sendReminderPayment() throws MessagingException {
        List<String> list=emailService.remindPayment();
        try {
            if(list.isEmpty()){
                return ApiResponse.<List<String>>builder().
                        message("No payment reminder emails to send.").
                        result(list).
                        build();
            }else{
                return ApiResponse.<List<String>>builder().
                        message("Payment reminder emails sent successfully.").
                        result(emailService.remindPayment()).
                        build();
            }
        }catch (MessagingException e){
            return ApiResponse.<List<String>>builder().
                    message("Failed to send mail: " + e.getMessage()).
                    build();
        }
    }


}