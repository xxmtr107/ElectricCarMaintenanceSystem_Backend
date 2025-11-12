package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus; // THÊM MỚI
import com.group02.ev_maintenancesystem.enums.EmailType;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import com.group02.ev_maintenancesystem.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j; // THÊM MỚI
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
import java.util.Objects; // THÊM MỚI

@Service
@Slf4j // THÊM MỚI
public class EmailServiceImpl implements EmailService {

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
    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private InvoiceRepository invoiceRepository; // <-- THÊM MỚI

    @Override
    @Scheduled(cron = "0 0 8 * * ?")
    public List<String> reminderMaintenance() throws MessagingException {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<String> receivers = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            // 1. Gọi service "thông minh" để lấy đề xuất
            List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());

            // 2. Nếu có đề xuất (tức là xe đến hạn)
            if (recommendations != null && !recommendations.isEmpty()) {
                MaintenanceRecommendationDTO recommendation = recommendations.get(0); // Lấy đề xuất đầu tiên
                int currentKm = vehicle.getCurrentKm();
                int serviceKm = recommendation.getMilestoneKm();
                String email = vehicle.getCustomerUser().getEmail();

                // 3. Kiểm tra xem đã gửi email cho mốc này với số km này chưa
                // (Tránh spam nếu xe không được cập nhật km và chạy cron mỗi ngày)
                int count = emailRepository.countByEmailAndTypeAndCurrentKmAndVehicleID(email, EmailType.KM, currentKm, vehicle.getId());
                if (count >= 1) {
                    continue; // Đã gửi rồi, bỏ qua
                }

                // 4. Gửi email
                Context context = new Context();
                context.setVariable("name", vehicle.getCustomerUser().getFullName());
                context.setVariable("vin", vehicle.getVin());
                context.setVariable("vehicle", vehicle.getModel().getName());
                context.setVariable("currentKm", currentKm);
                context.setVariable("serviceKm", serviceKm); // Mốc KM được đề xuất
                List<ServiceCenter> stations = getServiceCenters(vehicle.getId());
                context.setVariable("stations", stations);

                String htmlContent = templateEngine.process("mailForReminderKm", context);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
                helper.setTo(email);
                helper.setSubject("EV Maintenance System - Service Reminder");
                helper.setText(htmlContent, true);
                mailSender.send(message);

                // 5. Lưu lại email đã gửi
                saveEmail(email, EmailType.KM, currentKm, vehicle.getId(), null, null, null);
                receivers.add(email);
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

    @Override
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
            // để >=2 vì sẽ gửi 1 lần confirmAppointment
            if (count >= 2) continue;

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
            saveEmail(email, EmailType.APPOINTMENT_DATE,null,null,appointment.getId(), null, null);
            receivers.add(appointment.getCustomerUser().getEmail());
        }
        return receivers;
    }

    @Override
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
                int count=emailRepository.countByEmailAndTypeAndPaymentIDAndStatus(email,EmailType.PAYMENT,payment.getId(), EmailRecord.MailPaymentStatus.Not_Success);
                if(count >= 1) continue;

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
                saveEmail(email, EmailType.PAYMENT,null,null,null, payment.getId(), EmailRecord.MailPaymentStatus.Not_Success);
                receivers.add(payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getEmail());
            }
        }
        return receivers;
    }

    /**
     * ĐÃ CẬP NHẬT LOGIC:
     * Chuyển thành hàm public void, được gọi trực tiếp từ AppointmentServiceImpl.
     * Chỉ gửi mail nếu lịch hẹn ở trạng thái CONFIRMED và chưa được gửi.
     */
    @Transactional
    public void sendAppointmentConfirmation(Appointment appointment) {
        // Kiểm tra đầu vào
        if (appointment == null || appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            log.warn("sendAppointmentConfirmation called with invalid status or null appointment. Skipping.");
            return;
        }

        // Kiểm tra xem đã từng gửi mail CONFIRM cho lịch này CHƯA
        boolean alreadySent = emailRepository.existsByEmailAndTypeAndAppointmentID(
                appointment.getCustomerUser().getEmail(),
                EmailType.APPOINTMENT_DATE,
                appointment.getId()
        );

        if (!alreadySent) {
            try {
                Context context = new Context();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

                context.setVariable("name", appointment.getCustomerUser().getFullName());
                context.setVariable("dateTime", appointment.getAppointmentDate().format(formatter));
                context.setVariable("center", appointment.getServiceCenter().getName());
                context.setVariable("address", appointment.getServiceCenter().getAddress());
                context.setVariable("vehicle", appointment.getVehicle().getModel().getName());
                context.setVariable("phone", appointment.getServiceCenter().getPhone());

                String htmlContent = templateEngine.process("mailForAppointmentConfirmation", context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
                helper.setTo(appointment.getCustomerUser().getEmail());
                helper.setSubject("EV Maintenance System - Appointment Confirmation");
                helper.setText(htmlContent, true);
                mailSender.send(message);

                // Ghi lại email đã gửi
                saveEmail(appointment.getCustomerUser().getEmail(), EmailType.APPOINTMENT_DATE, null, null, appointment.getId(), null, null);
                log.info("Sent appointment confirmation email for appointment ID: {}", appointment.getId());
            } catch (Exception e) {
                log.error("Failed to send appointment confirmation email for ID {}: {}", appointment.getId(), e.getMessage());
            }
        }
    }


    /**
     * ĐÃ CẬP NHẬT LOGIC:
     * Chuyển thành hàm public void, được gọi trực tiếp từ VNPayServiceImpl.
     * Chỉ gửi mail nếu Hóa đơn ở trạng thái PAID và chưa được gửi.
     */
    @Transactional
    public void sendPaymentConfirmation(Invoice invoice) {
        // Kiểm tra đầu vào
        if (invoice == null || !"PAID".equals(invoice.getStatus())) {
            log.warn("sendPaymentConfirmation called with invalid status or null invoice. Skipping.");
            return;
        }

        String email = invoice
                .getMaintenanceRecord()
                .getAppointment()
                .getCustomerUser()
                .getEmail();

        // Tìm Payment ID tương ứng để ghi log
        Payment payment = invoice.getPayments().stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .findFirst() // Lấy payment PAID đầu tiên
                .orElse(null);

        if (payment == null) {
            log.warn("Invoice {} is PAID but has no corresponding PAID Payment entity. Skipping email.", invoice.getId());
            return;
        }

        Long paymentId = payment.getId();

        boolean alreadySent = emailRepository.existsByEmailAndTypeAndPaymentIDAndStatus(
                email,
                EmailType.PAYMENT,
                paymentId,
                EmailRecord.MailPaymentStatus.Success
        );

        if (!alreadySent) {
            try {
                Context context = new Context();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

                context.setVariable("name", invoice.getMaintenanceRecord().getAppointment().getCustomerUser().getFullName());
                context.setVariable("vin", invoice.getMaintenanceRecord().getAppointment().getVehicle().getVin());
                context.setVariable("vehicle", invoice.getMaintenanceRecord().getAppointment().getVehicle().getModel().getName());
                context.setVariable("invoiceNo", invoice.getId());
                context.setVariable("amount", invoice.getTotalAmount());
                context.setVariable("paidDate", LocalDateTime.now().format(formatter));

                String htmlContent = templateEngine.process("mailForPaymentConfirmation", context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
                helper.setTo(email);
                helper.setSubject("EV Maintenance System - Payment Confirmation");
                helper.setText(htmlContent, true);
                mailSender.send(message);

                // Ghi lại email đã gửi
                saveEmail(email, EmailType.PAYMENT, null, null,
                        null, paymentId, EmailRecord.MailPaymentStatus.Success);
                log.info("Sent payment confirmation email for invoice ID: {}", invoice.getId());
            } catch (Exception e) {
                log.error("Failed to send payment confirmation email for invoice ID {}: {}", invoice.getId(), e.getMessage());
            }
        }
    }

    private void saveEmail(String email, EmailType emailType,Integer currentKm,Long vehicleID,Long appointmentID,Long paymentID,EmailRecord.MailPaymentStatus status) {
        EmailRecord mail = EmailRecord.builder().
                email(email).
                type(emailType).
                sentTime(LocalDateTime.now()).
                vehicleID(vehicleID).
                appointmentID(appointmentID).
                currentKm(currentKm).
                paymentID(paymentID).
                status(status).
                build();
        emailRepository.save(mail);
    }

    private boolean alreadySentRecently(String email, EmailType emailType, int days) {
        LocalDateTime sinceTime = LocalDateTime.now().minusDays(days);
        return emailRepository.findByEmailAndTypeAndSentTimeAfter(email, emailType, sinceTime).isPresent();
    }
}