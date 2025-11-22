package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.EmailType;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import com.group02.ev_maintenancesystem.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    // private final Resend resend; // <-- XÓA DÒNG NÀY
    private final RestTemplate restTemplate = new RestTemplate(); // Khởi tạo trực tiếp hoặc Inject nếu đã có Bean

    private final TemplateEngine templateEngine;
    private final VehicleRepository vehicleRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceCenterRepository serviceCenterRepository;
    private final PaymentRepository paymentRepository;
    private final EmailRepository emailRepository;
    private final MaintenanceService maintenanceService;

    @Value("${resend.api-key}")
    private String apiKey; // Lấy API Key từ file config

    @Value("${resend.from-email}")
    private String fromEmail;

    @PostConstruct
    public void init() {
        log.info("=== EMAIL SERVICE INITIALIZED (USING REST TEMPLATE) ===");
    }

    // --- HÀM GỬI MAIL MỚI DÙNG REST TEMPLATE ---
    private void sendEmailViaResend(String to, String subject, String htmlContent) {
        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // --- CẤU HÌNH CHÍNH THỨC (PRODUCTION) ---
        Map<String, Object> body = new HashMap<>();

        // 1. From: Lấy từ config (đã verify domain)
        // Ví dụ: "NTNTB System <no-reply@yourdomain.com>"
        body.put("from", "NTNTB System <" + fromEmail + ">");

        // 2. To: Gửi trực tiếp cho khách hàng (tham số 'to')
        body.put("to", Collections.singletonList(to));

        // 3. Subject & Content: Giữ nguyên
        body.put("subject", subject);
        body.put("html", htmlContent);
        // -----------------------------------------

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully to [{}]. ID: {}", to, response.getBody());
            } else {
                log.error("Failed to send email to [{}]. Status: {}, Body: {}", to, response.getStatusCode(), response.getBody());
            }
        } catch (RestClientException e) {
            log.error("Exception when calling Resend API for [{}]: {}", to, e.getMessage());
        }
    }

    // =========================================================================
    // CÁC HÀM LOGIC NGHIỆP VỤ BÊN DƯỚI GIỮ NGUYÊN
    // (Chỉ cần đảm bảo chúng gọi hàm sendEmailViaResend ở trên)
    // =========================================================================

    @Override
    @Scheduled(cron = "0 0 8 * * ?")
    public List<String> reminderMaintenance() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<String> receivers = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());

            if (recommendations != null && !recommendations.isEmpty()) {
                MaintenanceRecommendationDTO recommendation = recommendations.get(0);
                int currentKm = vehicle.getCurrentKm();
                int serviceKm = recommendation.getMilestoneKm();
                String email = vehicle.getCustomerUser().getEmail();

                int count = emailRepository.countByEmailAndTypeAndCurrentKmAndVehicleID(email, EmailType.KM, currentKm, vehicle.getId());
                if (count >= 1) continue;

                Context context = new Context();
                context.setVariable("name", vehicle.getCustomerUser().getFullName());
                context.setVariable("vin", vehicle.getVin());
                context.setVariable("vehicle", vehicle.getModel().getName());
                context.setVariable("currentKm", currentKm);
                context.setVariable("serviceKm", serviceKm);
                List<ServiceCenter> stations = getServiceCenters(vehicle.getId());
                context.setVariable("stations", stations);

                String htmlContent = templateEngine.process("mailForReminderKm", context);

                // GỌI HÀM MỚI
                sendEmailViaResend(email, "EV Maintenance System - Service Reminder", htmlContent);

                saveEmail(email, EmailType.KM, currentKm, vehicle.getId(), null, null, null, null, null);
                receivers.add(email);
            }
        }
        return receivers;
    }

    @Override
    @Scheduled(cron = "0 0 8 * * ?")
    public List<String> upcomingAppointment() {
        List<String> receivers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = tomorrow.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(tomorrow, endOfTomorrow);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (Appointment appointment : appointments) {
            String email = appointment.getCustomerUser().getEmail();
            if (alreadySentRecently(email, EmailType.APPOINTMENT_REMINDER, 1)) continue;
            int count = emailRepository.countByEmailAndTypeAndAppointmentID(email, EmailType.APPOINTMENT_REMINDER, appointment.getId());
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

            // GỌI HÀM MỚI
            sendEmailViaResend(email, "EV Maintenance System - Appointment Reminder", htmlContent);

            saveEmail(email, EmailType.APPOINTMENT_REMINDER, null, null, appointment.getId(), null, appointment.getCustomerUser().getId(), null, null);
            receivers.add(email);
        }
        return receivers;
    }

    @Override
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public List<String> remindPayment() {
        List<String> receivers = new ArrayList<>();
        List<Payment> list = paymentRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (Payment payment : list) {
            if (payment.getStatus().equals(PaymentStatus.UN_PAID) || payment.getStatus().equals(PaymentStatus.FAILED)) {
                String email = payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getEmail();
                int count = emailRepository.countByEmailAndTypeAndPaymentIDAndStatus(email, EmailType.PAYMENT_REMINDER, payment.getId(), EmailRecord.MailPaymentStatus.Not_Success);
                if (count >= 1) continue;

                Context context = new Context();
                context.setVariable("name", payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getFullName());
                context.setVariable("vin", payment.getInvoice().getMaintenanceRecord().getAppointment().getVehicle().getVin());
                context.setVariable("vehicle", payment.getInvoice().getMaintenanceRecord().getAppointment().getVehicle().getModel().getName());
                context.setVariable("invoiceNo", payment.getInvoice().getId());
                context.setVariable("amount", payment.getInvoice().getTotalAmount());
                context.setVariable("dueDate", payment.getInvoice().getMaintenanceRecord().getAppointment().getAppointmentDate().format(formatter));

                String htmlContent = templateEngine.process("mailForReminderPayment", context);

                // GỌI HÀM MỚI
                sendEmailViaResend(email, "EV Maintenance System - Checkout Reminder", htmlContent);

                saveEmail(email, EmailType.PAYMENT_REMINDER, null, null, null, payment.getId(), payment.getInvoice().getMaintenanceRecord().getAppointment().getCustomerUser().getId(), null, EmailRecord.MailPaymentStatus.Not_Success);
                receivers.add(email);
            }
        }
        return receivers;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAppointmentConfirmation(Appointment appointment) {
        if (appointment == null || appointment.getStatus() != AppointmentStatus.PENDING) return;

        boolean alreadySent = emailRepository.existsByEmailAndTypeAndAppointmentID(
                appointment.getCustomerUser().getEmail(), EmailType.APPOINTMENT_CONFIRMATION, appointment.getId());

        if (!alreadySent) {
            Context context = new Context();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            context.setVariable("name", appointment.getCustomerUser().getFullName());
            context.setVariable("dateTime", appointment.getAppointmentDate().format(formatter));
            context.setVariable("center", appointment.getServiceCenter().getName());
            context.setVariable("address", appointment.getServiceCenter().getAddress());
            context.setVariable("vin", appointment.getVehicle().getVin());
            context.setVariable("vehicle", appointment.getVehicle().getModel().getName());
            context.setVariable("phone", appointment.getServiceCenter().getPhone());

            String htmlContent = templateEngine.process("mailForAppointmentConfirmation", context);

            // GỌI HÀM MỚI
            sendEmailViaResend(appointment.getCustomerUser().getEmail(), "EV Maintenance System - Appointment Confirmation", htmlContent);

            saveEmail(appointment.getCustomerUser().getEmail(), EmailType.APPOINTMENT_CONFIRMATION, null, null, appointment.getId(), null, appointment.getCustomerUser().getId(), null, null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPaymentConfirmation(Invoice invoice) {
        if (invoice == null || !"PAID".equals(invoice.getStatus())) return;

        String email = invoice.getMaintenanceRecord().getAppointment().getCustomerUser().getEmail();
        Payment payment = invoice.getPayments().stream().filter(p -> p.getStatus() == PaymentStatus.PAID).findFirst().orElse(null);
        if (payment == null) return;

        Long paymentId = payment.getId();
        boolean alreadySent = emailRepository.existsByEmailAndTypeAndPaymentIDAndStatus(email, EmailType.PAYMENT_CONFIRMATION, paymentId, EmailRecord.MailPaymentStatus.Success);

        if (!alreadySent) {
            Context context = new Context();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            context.setVariable("name", invoice.getMaintenanceRecord().getAppointment().getCustomerUser().getFullName());
            context.setVariable("vin", invoice.getMaintenanceRecord().getAppointment().getVehicle().getVin());
            context.setVariable("vehicle", invoice.getMaintenanceRecord().getAppointment().getVehicle().getModel().getName());
            context.setVariable("invoiceNo", invoice.getId());
            context.setVariable("amount", invoice.getTotalAmount());
            context.setVariable("paidDate", LocalDateTime.now().format(formatter));

            String htmlContent = templateEngine.process("mailForPaymentConfirmation", context);

            // GỌI HÀM MỚI
            sendEmailViaResend(email, "EV Maintenance System - Payment Confirmation", htmlContent);

            saveEmail(email, EmailType.PAYMENT_CONFIRMATION, null, null, invoice.getMaintenanceRecord().getAppointment().getId(), paymentId,
                    invoice.getMaintenanceRecord().getAppointment().getCustomerUser().getId(), null, EmailRecord.MailPaymentStatus.Success);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAccountCreationEmail(User user, String rawPassword) {
        if (user == null || user.getEmail() == null) return;

        Context context = new Context();
        context.setVariable("name", user.getFullName());
        context.setVariable("username", user.getUsername());
        context.setVariable("password", rawPassword);

        String htmlContent = templateEngine.process("mailForAccountCreation", context);

        // GỌI HÀM MỚI
        sendEmailViaResend(user.getEmail(), "EV Maintenance System - Account Created Successfully", htmlContent);

        saveEmail(user.getEmail(), EmailType.ACCOUNT_CREATION, null, null, null, null, user.getId(), null, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendConfirmAndAssignAppointment(Appointment appointment) {
        if (appointment == null || appointment.getStatus() != AppointmentStatus.CONFIRMED) return;

        boolean alreadySent = emailRepository.existsByEmailAndTypeAndAppointmentIDAndTechnicianID(
                appointment.getCustomerUser().getEmail(), EmailType.APPOINTMENT_CONFIRMED_AND_ASSIGNED_SUCCESS, appointment.getId(), appointment.getTechnicianUser().getId());

        if (!alreadySent) {
            Context context = new Context();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            context.setVariable("name", appointment.getCustomerUser().getFullName());
            context.setVariable("dateTime", appointment.getAppointmentDate().format(formatter));
            context.setVariable("center", appointment.getServiceCenter().getName());
            context.setVariable("address", appointment.getServiceCenter().getAddress());
            context.setVariable("vin", appointment.getVehicle().getVin());
            context.setVariable("phone", appointment.getServiceCenter().getPhone());
            context.setVariable("vehicle", appointment.getVehicle().getModel().getName());

            if (appointment.getTechnicianUser() != null) {
                context.setVariable("technicianName", appointment.getTechnicianUser().getFullName());
                context.setVariable("technicianPhone", appointment.getTechnicianUser().getPhone());
            } else {
                context.setVariable("technicianName", "To be assigned");
                context.setVariable("technicianPhone", "N/A");
            }

            String htmlContent = templateEngine.process("mailForConfirmAndAssignAppointment", context);

            // GỌI HÀM MỚI
            sendEmailViaResend(appointment.getCustomerUser().getEmail(), "EV Maintenance System - Appointment Confirmed & Technician Assigned", htmlContent);

            saveEmail(appointment.getCustomerUser().getEmail(), EmailType.APPOINTMENT_CONFIRMED_AND_ASSIGNED_SUCCESS, null, null, appointment.getId(), null, appointment.getCustomerUser().getId(), appointment.getTechnicianUser().getId(), null);
        }
    }

    @Override
    public void sendPasswordResetEmail(User user, String token) {
        if (user == null || user.getEmail() == null || token == null) return;

        String frontendBaseUrl = "https://electric-car-maintenance.vercel.app";
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token;

        Context context = new Context();
        context.setVariable("name", user.getFullName());
        context.setVariable("url", resetUrl);

        String htmlContent = templateEngine.process("mailForPasswordReset", context);

        // GỌI HÀM MỚI
        sendEmailViaResend(user.getEmail(), "EV Maintenance System - Yêu cầu Đặt lại Mật khẩu", htmlContent);

        saveEmail(user.getEmail(), EmailType.PASSWORD_RESET_REQUEST, null, null, null, null, user.getId(), null, null);
    }

    // Giữ nguyên các hàm helper
    public List<ServiceCenter> getServiceCenters(Long vehicleId) {
        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
        Map<Long, ServiceCenter> map = new LinkedHashMap<>();
        for (Appointment appointment : appointments) {
            ServiceCenter sc = serviceCenterRepository.findServiceCenterByAppointments_Id(appointment.getId());
            if (sc != null) map.putIfAbsent(sc.getId(), sc);
        }
        return new ArrayList<>(map.values());
    }

    private void saveEmail(String email, EmailType emailType, Integer currentKm, Long vehicleID, Long appointmentID, Long paymentID, Long customerID, Long technicianID, EmailRecord.MailPaymentStatus status) {
        EmailRecord mail = EmailRecord.builder()
                .email(email)
                .type(emailType)
                .sentTime(LocalDateTime.now())
                .vehicleID(vehicleID)
                .appointmentID(appointmentID)
                .currentKm(currentKm)
                .paymentID(paymentID)
                .customerID(customerID)
                .technicianID(technicianID)
                .status(status)
                .build();
        emailRepository.save(mail);
    }

    private boolean alreadySentRecently(String email, EmailType emailType, int days) {
        LocalDateTime sinceTime = LocalDateTime.now().minusDays(days);
        return emailRepository.findByEmailAndTypeAndSentTimeAfter(email, emailType, sinceTime).isPresent();
    }
}