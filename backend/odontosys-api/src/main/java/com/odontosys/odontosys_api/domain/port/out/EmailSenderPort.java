package com.odontosys.odontosys_api.domain.port.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Puerto Secundario — Contrato para envío de correos electrónicos.
 */
public interface EmailSenderPort {

    void sendPasswordResetCode(String toEmail, String code, int expirationMinutes);

    void sendAppointmentConfirmationNotification(String toEmail, String patientName, String dentistName, LocalDate date, LocalTime startTime);

    void sendAppointmentRescheduledNotification(String toEmail, String patientName, String dentistName, LocalDate newDate, LocalTime newStartTime);

    void sendAppointmentCancelledNotification(String toEmail, String patientName, String dentistName, LocalDate date, LocalTime startTime, String reason);

    void sendInvoiceReceiptNotification(String toEmail, String patientName, String invoiceNumber, BigDecimal totalAmount, BigDecimal paidAmount, String invoiceStatus, List<String> itemsSummary, String paymentMethodName);
}
