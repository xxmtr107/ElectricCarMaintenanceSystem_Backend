package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.EmailType;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import com.group02.ev_maintenancesystem.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    ServiceCenterRepository serviceCenterRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    EmailRepository emailRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    public List<String> reminderKm() throws MessagingException {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<String> receivers = new ArrayList<>();

        int[] serviceKmList = {12000, 24000, 36000, 48000, 60000, 72000, 84000, 96000, 108000, 120000};

        for (Vehicle vehicle : vehicles) {
            int currentKm = vehicle.getCurrentKm();

            for (int serviceKm : serviceKmList) {
                if (currentKm >= serviceKm - 100 && currentKm <= serviceKm + 100) {

                    String email = vehicle.getCustomerUser().getEmail();

                    //gửi mail tiếp theo sau 3 ngày
                    if (alreadySentRecently(email, EmailType.KM, 3)) break;
                    //mail này chỉ gửi 2 lần duy nhất
                    int count = emailRepository.countByEmailAndTypeAndCurrentKm(email, EmailType.KM,vehicle.getCurrentKm());
                    if (count >= 2) break;

                    Context context = new Context();
                    context.setVariable("name", vehicle.getCustomerUser().getFullName());
                    context.setVariable("vin", vehicle.getVin());
                    context.setVariable("vehicle", vehicle.getModel().getName());
                    context.setVariable("currentKm", currentKm);
                    context.setVariable("serviceKm", serviceKm);
                    List<ServiceCenter> stations = getServiceCenters(vehicle.getId());
                    context.setVariable("stations", stations);
                    String htmlContent = templateEngine.process("mailForReminderKm", context);
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
                    helper.setTo(email);
                    helper.setSubject("EV Maintenance System - Service Reminder");
                    helper.setText(htmlContent, true);
                    mailSender.send(message);
                    saveEmail(email, EmailType.KM,vehicle.getCurrentKm(),null);
                    receivers.add(email);
                    break;
                }
            }
        }
        return receivers;
    }


    public List<ServiceCenter> getServiceCenters(Long vehicleId) {
        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
        List<ServiceCenter> list = new ArrayList<>();
        for (Appointment appointment : appointments) {
            list.add(serviceCenterRepository.findServiceCenterByAppointments_Id(appointment.getId()));
        }
        return list;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public List<String> upcomingAppointment() throws MessagingException {
        List<String> receivers = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = tomorrow.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(tomorrow, endOfTomorrow);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (Appointment appointment : appointments) {

            String email = appointment.getCustomerUser().getEmail();
            if (alreadySentRecently(email, EmailType.APPOINTMENT_DATE, 1)) continue;
            int count = emailRepository.countByEmailAndTypeAndAppointmentID(email, EmailType.APPOINTMENT_DATE,appointment.getId());
            if (count >= 1) continue;

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
            helper.setTo(email);
            helper.setSubject("EV Maintenance System - Appointment Reminder");
            helper.setText(htmlContent, true);
            mailSender.send(message);
            saveEmail(email, EmailType.APPOINTMENT_DATE,null,appointment.getId());
            receivers.add(appointment.getCustomerUser().getEmail());
        }
        return receivers;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public List<String> remindPayment() throws MessagingException {
        List<String> receivers = new ArrayList<>();
        List<Payment> list = paymentRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (Payment payment : list) {
            if (payment.getStatus().equals(PaymentStatus.UN_PAID) ||
                    payment.getStatus().equals(PaymentStatus.FAILED)) {

                String email = payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getEmail();
                if (alreadySentRecently(email, EmailType.PAYMENT, 2)) continue;

                Context context = new Context();
                context.setVariable("name", payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getFullName());
                context.setVariable("vin", payment.getInvoice().getMaintenanceRecord().getAppointment().getVehicle().getVin());
                context.setVariable("vehicle", payment.getInvoice().getMaintenanceRecord().getAppointment().getVehicle().getModel().getName());
                context.setVariable("invoiceNo", payment.getInvoice().getId());
                context.setVariable("amount", payment.getInvoice().getTotalAmount());
                // DUE DATE IS APPOINTMENT DATE
                context.setVariable("dueDate", payment.getInvoice().getMaintenanceRecord().getAppointment().getAppointmentDate().format(formatter));

                String htmlContent = templateEngine.process("mailForReminderPayment", context);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
                helper.setTo(email);
                helper.setSubject("EV Maintenance System - Checkout Reminder");
                helper.setText(htmlContent, true);
                mailSender.send(message);
                saveEmail(email, EmailType.PAYMENT,null,null);
                receivers.add(payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getEmail());
            }
        }
        return receivers;
    }

    private void saveEmail(String email, EmailType emailType,Integer currentKm,Long appointmentID) {
        EmailRecord mail = EmailRecord.builder().
                email(email).
                type(emailType).
                sentTime(LocalDateTime.now()).
                appointmentID(appointmentID).
                currentKm(currentKm).
                build();
        emailRepository.save(mail);
    }

    private boolean alreadySentRecently(String email, EmailType emailType, int days) {
        LocalDateTime sinceTime = LocalDateTime.now().minusDays(days);
        return emailRepository.findByEmailAndTypeAndSentTimeAfter(email, emailType, sinceTime).isPresent();
    }
}