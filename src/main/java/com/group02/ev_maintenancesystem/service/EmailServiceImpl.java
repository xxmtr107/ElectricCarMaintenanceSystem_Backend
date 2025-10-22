package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import com.group02.ev_maintenancesystem.entity.Vehicle;
import com.group02.ev_maintenancesystem.repository.AppointmentRepository;
import com.group02.ev_maintenancesystem.repository.ServiceCenterRepository;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
//    @Autowired
//    ServiceCenterRepository serviceCenterRepository;

//    public List<String> reminderKm()
//            throws MessagingException {
//
//        List<Vehicle> vehicles = vehicleRepository.findAll();
//        List<String> receivers = new ArrayList<>();
//        for (Vehicle vehicle : vehicles) {
//            if (vehicle.getCurrentKm() >= 9900 && vehicle.getCurrentKm() <= 10100) {
//                Context context = new Context();
//                context.setVariable("name", vehicle.getCustomerUser().getFullName());
//                context.setVariable("vin", vehicle.getVin());
//                context.setVariable("vehicle", vehicle.getModel().getName());
//                context.setVariable("currentKm", vehicle.getCurrentKm());
//                context.setVariable("serviceKm", 10000);
//                List<ServiceCenter> stations = getServiceCenters(vehicle.getId());
//                context.setVariable("stations", stations);
//                String htmlContent = templateEngine.process("mailForReminderKm", context);
//                MimeMessage message = mailSender.createMimeMessage();
//                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
//
//                helper.setTo(vehicle.getCustomerUser().getEmail());
//                helper.setSubject("EV Maintenance System - Service Reminder");
//                helper.setText(htmlContent, true);
//                mailSender.send(message);
//                receivers.add(vehicle.getCustomerUser().getEmail());
//            }
//        }
//        return receivers;
//    }
//
//    public List<ServiceCenter> getServiceCenters(Long vehicleId) {
//        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
//        List<ServiceCenter> list = new ArrayList<>();
//        for (Appointment appointment : appointments) {
//            list.add(serviceCenterRepository.findServiceCenterByAppointments_Id(appointment.getId()));
//        }
//        return list;
//    }

//    @Scheduled(cron="0 0 8 * * ?")
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
            context.setVariable("email", appointment.getServiceCenter().getEmail());
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