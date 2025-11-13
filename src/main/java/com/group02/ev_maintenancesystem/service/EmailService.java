package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.Invoice;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import com.group02.ev_maintenancesystem.entity.User;
import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {
    List<String> reminderMaintenance() throws MessagingException;
    List<ServiceCenter> getServiceCenters(Long vehicleId);
    List<String> upcomingAppointment() throws MessagingException;
    List<String> remindPayment() throws MessagingException;
    void sendAppointmentConfirmation(Appointment appointment);
    void sendPaymentConfirmation(Invoice invoice);
    void sendAccountCreationEmail(User user, String rawPassword);
    void sendConfirmAndAssignAppointment(Appointment appointment);
}
