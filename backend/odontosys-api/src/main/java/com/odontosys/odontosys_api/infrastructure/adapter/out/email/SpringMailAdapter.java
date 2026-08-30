package com.odontosys.odontosys_api.infrastructure.adapter.out.email;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import jakarta.mail.internet.MimeMessage;

@Component
public class SpringMailAdapter implements EmailSenderPort {

    private static final Logger log = LoggerFactory.getLogger(SpringMailAdapter.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public SpringMailAdapter(JavaMailSender mailSender,
                             @Value("${spring.mail.username:noreply@odontosys.com}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendPasswordResetCode(String toEmail, String code, int expirationMinutes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("OdontoSys — Código de Recuperación de Contraseña");

            String htmlBody = "<html><body style=\"font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px;\">"
                    + "<div style=\"max-width: 500px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);\">"
                    + "<h2 style=\"color: #0d6efd; text-align: center; margin-top: 0;\">OdontoSys</h2>"
                    + "<h3 style=\"color: #333333; text-align: center;\">Recuperación de Contraseña</h3>"
                    + "<p style=\"color: #555555; font-size: 15px;\">Has solicitado restablecer la contraseña de tu cuenta. Usa el siguiente código de verificación:</p>"
                    + "<div style=\"background: #0d6efd; color: #ffffff; font-size: 28px; font-weight: bold; letter-spacing: 5px; text-align: center; padding: 15px; border-radius: 8px; margin: 25px 0;\">"
                    + code
                    + "</div>"
                    + "<p style=\"color: #777777; font-size: 13px; text-align: center;\">Este código expira en <b>" + expirationMinutes + " minutos</b>.</p>"
                    + "<hr style=\"border: none; border-top: 1px solid #eeeeee; margin: 20px 0;\" />"
                    + "<p style=\"color: #999999; font-size: 12px; text-align: center;\">Si no solicitaste este cambio, puedes ignorar este mensaje de forma segura.</p>"
                    + "</div></body></html>";

            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Correo de verificación enviado exitosamente a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar el correo a {} (Verifica la configuración SMTP). Código simulado: {}", toEmail, code);
        }
    }

    @Override
    public void sendAppointmentConfirmationNotification(String toEmail, String patientName, String dentistName, LocalDate date, LocalTime startTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("OdontoSys — Confirmación de Cita Odontológica");

            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String formattedTime = startTime.format(DateTimeFormatter.ofPattern("hh:mm a"));

            String htmlBody = "<html><body style=\"font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px;\">"
                    + "<div style=\"max-width: 500px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px;\">"
                    + "<h2 style=\"color: #0d6efd; text-align: center;\">OdontoSys</h2>"
                    + "<h3 style=\"color: #198754; text-align: center;\">¡Cita Agendada Con Éxito!</h3>"
                    + "<p>Hola <b>" + patientName + "</b>,</p>"
                    + "<p>Tu cita odontológica ha sido agendada exitosamente en nuestro sistema.</p>"
                    + "<div style=\"background: #e9ecef; padding: 15px; border-radius: 8px; margin: 20px 0;\">"
                    + "<p style=\"margin: 5px 0;\"><b>Odontólogo:</b> " + dentistName + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Fecha:</b> " + formattedDate + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Hora:</b> " + formattedTime + "</p>"
                    + "</div>"
                    + "<p style=\"color: #777777; font-size: 13px;\">Te esperamos con gusto en nuestras instalaciones.</p>"
                    + "</div></body></html>";

            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Correo de confirmación de cita enviado a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar correo de confirmación a {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendAppointmentRescheduledNotification(String toEmail, String patientName, String dentistName, LocalDate newDate, LocalTime newStartTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("OdontoSys — Modificación de Cita Odontológica");

            String formattedDate = newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String formattedTime = newStartTime.format(DateTimeFormatter.ofPattern("hh:mm a"));

            String htmlBody = "<html><body style=\"font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px;\">"
                    + "<div style=\"max-width: 500px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px;\">"
                    + "<h2 style=\"color: #0d6efd; text-align: center;\">OdontoSys</h2>"
                    + "<h3 style=\"color: #ffc107; text-align: center;\">Cambio de Hora en tu Cita</h3>"
                    + "<p>Hola <b>" + patientName + "</b>,</p>"
                    + "<p>Te informamos que tu cita odontológica ha sido <b>reagendada / modificada</b> en nuestro sistema.</p>"
                    + "<div style=\"background: #fff3cd; color: #664d03; padding: 15px; border-radius: 8px; margin: 20px 0;\">"
                    + "<p style=\"margin: 5px 0;\"><b>NUEVO HORARIO ASIGNADO:</b></p>"
                    + "<p style=\"margin: 5px 0;\"><b>Odontólogo:</b> " + dentistName + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Nueva Fecha:</b> " + formattedDate + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Nueva Hora:</b> " + formattedTime + "</p>"
                    + "</div>"
                    + "<p style=\"color: #777777; font-size: 13px;\">Si tienes dudas sobre este cambio, por favor comunícate con la clínica.</p>"
                    + "</div></body></html>";

            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Correo de reagendamiento de cita enviado a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar correo de cambio de hora a {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendAppointmentCancelledNotification(String toEmail, String patientName, String dentistName, LocalDate date, LocalTime startTime, String reason) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("OdontoSys — Cancelación de Cita Odontológica");

            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String formattedTime = startTime.format(DateTimeFormatter.ofPattern("hh:mm a"));

            String htmlBody = "<html><body style=\"font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px;\">"
                    + "<div style=\"max-width: 500px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px;\">"
                    + "<h2 style=\"color: #0d6efd; text-align: center;\">OdontoSys</h2>"
                    + "<h3 style=\"color: #dc3545; text-align: center;\">Cita Cancelada</h3>"
                    + "<p>Hola <b>" + patientName + "</b>,</p>"
                    + "<p>Te informamos que tu cita odontológica programada con " + dentistName + " para el <b>" + formattedDate + "</b> a las <b>" + formattedTime + "</b> ha sido cancelada.</p>"
                    + (reason != null && !reason.isBlank() ? "<p><b>Motivo:</b> " + reason + "</p>" : "")
                    + "<p style=\"color: #777777; font-size: 13px;\">Puedes comunicarte con nosotros si deseas agendar un nuevo horario.</p>"
                    + "</div></body></html>";

            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Correo de cancelación de cita enviado a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar correo de cancelación a {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendInvoiceReceiptNotification(String toEmail, String patientName, String invoiceNumber, BigDecimal totalAmount, BigDecimal paidAmount, String invoiceStatus, List<String> itemsSummary, String paymentMethodName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("OdontoSys — Comprobante de Pago de Factura " + invoiceNumber);

            StringBuilder itemsHtml = new StringBuilder();
            if (itemsSummary != null) {
                for (String item : itemsSummary) {
                    itemsHtml.append("<li style=\"margin: 5px 0;\">").append(item).append("</li>");
                }
            }

            String htmlBody = "<html><body style=\"font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px;\">"
                    + "<div style=\"max-width: 550px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);\">"
                    + "<h2 style=\"color: #0d6efd; text-align: center; margin-top: 0;\">OdontoSys</h2>"
                    + "<h3 style=\"color: #198754; text-align: center;\">Comprobante de Pago / Factura</h3>"
                    + "<p>Hola <b>" + patientName + "</b>,</p>"
                    + "<p>Gracias por tu pago en <b>OdontoSys</b>. A continuación detallamos tu factura y recibo:</p>"
                    + "<div style=\"background: #e9ecef; padding: 15px; border-radius: 8px; margin: 20px 0;\">"
                    + "<p style=\"margin: 5px 0;\"><b>N° Factura:</b> " + invoiceNumber + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Estado:</b> <span style=\"color: #198754; font-weight: bold;\">" + invoiceStatus + "</span></p>"
                    + "<p style=\"margin: 5px 0;\"><b>Método de Pago:</b> " + paymentMethodName + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Monto Total Facturado:</b> $" + totalAmount + "</p>"
                    + "<p style=\"margin: 5px 0;\"><b>Monto Pagado:</b> $" + paidAmount + "</p>"
                    + "</div>"
                    + "<h4>Detalle de Servicios y Tratamientos:</h4>"
                    + "<ul style=\"padding-left: 20px;\">" + itemsHtml.toString() + "</ul>"
                    + "<hr style=\"border: none; border-top: 1px solid #eeeeee; margin: 20px 0;\" />"
                    + "<p style=\"color: #777777; font-size: 13px; text-align: center;\">¡Gracias por confiar tu salud oral en OdontoSys!</p>"
                    + "</div></body></html>";

            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Correo de comprobante/factura HTML enviado a {}", toEmail);
        } catch (Exception e) {
            log.warn("No se pudo enviar recibo/factura HTML a {}: {}", toEmail, e.getMessage());
        }
    }
}
