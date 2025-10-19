package com.group02.ev_maintenancesystem.controller;


import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/mail")
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/send-reminder")
    public ApiResponse<String> sendReminder(@RequestParam String to) throws MessagingException {
        emailService.sendHtmlMail(
                to,
                "Vehicle Service Appointment Reminder",
                "serviceReminder"
        );
        return ApiResponse.<String>builder().
                message("Mail sent successfully.").
                build();
    }
}
