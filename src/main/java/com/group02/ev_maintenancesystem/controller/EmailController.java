package com.group02.ev_maintenancesystem.controller;


import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.util.List;

@RestController
@RequestMapping("/mails")
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/Km")
    public ApiResponse<List<String> >sendReminderKm() throws MessagingException {
        List<String> list=emailService.reminderKm();
        try {
            if(list.isEmpty()){
                return ApiResponse.<List<String>>builder().
                        message("No emails to send.").
                        result(list).
                        build();
            }else{
                return ApiResponse.<List<String>>builder().
                        message("Mail sent successfully.").
                        result(emailService.reminderKm()).
                        build();
            }
        }catch (MessagingException e){
            return ApiResponse.<List<String>>builder().
                    message("Failed to send mail: " + e.getMessage()).
                    build();
        }
    }

    @PostMapping("/Schedule")
    public ApiResponse<List<String>> sendReminderSchedule() throws MessagingException {
        List<String> list=emailService.upcomingAppointment();
        try {
            if(list.isEmpty()){
                return ApiResponse.<List<String>>builder().
                        message("No emails to send.").
                        result(list).
                        build();
            }else{
                return ApiResponse.<List<String>>builder().
                        message("Mail sent successfully.").
                        result(emailService.upcomingAppointment()).
                        build();
            }
        }catch (MessagingException e){
            return ApiResponse.<List<String>>builder().
                    message("Failed to send mail: " + e.getMessage()).
                    build();
        }
    }

    @PostMapping("/Pay")
    public ApiResponse<List<String>> sendReminderCheckout() throws MessagingException {
        List<String> list=emailService.remindPayment();
        try {
            if(list.isEmpty()){
                return ApiResponse.<List<String>>builder().
                        message("No emails to send.").
                        result(list).
                        build();
            }else{
                return ApiResponse.<List<String>>builder().
                        message("Mail sent successfully.").
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
