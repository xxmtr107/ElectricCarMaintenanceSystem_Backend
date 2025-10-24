package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import com.group02.ev_maintenancesystem.entity.Vehicle;
import com.group02.ev_maintenancesystem.repository.AppointmentRepository;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl {

    JavaMailSender mailSender;

    TemplateEngine templateEngine;

    VehicleRepository vehicleRepository;

    AppointmentRepository appointmentRepository;

    public List<String> mailUpcomingAppointment() throws MessagingException {
        List<String> receivers = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = tomorrow.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(tomorrow, endOfTomorrow);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (Appointment appointment : appointments) {
            Context context = new Context();
            context.setVariable("name", appointment.getCustomerUser().getFullName());
            String formattedDateTime = appointment.getAppointmentDate().format(formatter);
            context.setVariable("dateTime", formattedDateTime);
            context.setVariable("center", appointment.getServiceCenter().getName());
            context.setVariable("address", appointment.getServiceCenter().getAddress());
            context.setVariable("vehicle", appointment.getVehicle().getModel().getName());
            context.setVariable("phone", appointment.getServiceCenter().getPhone());
            String htmlContent = templateEngine.process("mailForReminderSchedule", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(appointment.getCustomerUser().getEmail());
            helper.setSubject("EV Maintenance System - Appointment Reminder");
            helper.setText(htmlContent, true);
            mailSender.send(message);
            receivers.add(appointment.getCustomerUser().getEmail());
        }
        return receivers;
    }
}