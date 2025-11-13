package com.group02.ev_maintenancesystem.service;

import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {
    List<String> reminderKm() throws MessagingException;
    List<String> upcomingAppointment() throws MessagingException;
    List<String> remindPayment() throws MessagingException;
    List<String> sendAppointmentConfirmation() throws MessagingException;
    List<String> sendPaymentConfirmation() throws MessagingException;
}
