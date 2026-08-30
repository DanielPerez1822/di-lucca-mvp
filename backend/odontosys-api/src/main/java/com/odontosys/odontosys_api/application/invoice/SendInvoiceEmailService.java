package com.odontosys.odontosys_api.application.invoice;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.exception.InvoiceNotFoundException;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.port.in.invoice.SendInvoiceEmailUseCase;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;

public class SendInvoiceEmailService implements SendInvoiceEmailUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final PatientRepositoryPort patientRepository;
    private final PaymentRepositoryPort paymentRepository;
    private final EmailSenderPort emailSender;

    public SendInvoiceEmailService(InvoiceRepositoryPort invoiceRepository,
                                  PatientRepositoryPort patientRepository,
                                  PaymentRepositoryPort paymentRepository,
                                  EmailSenderPort emailSender) {
        this.invoiceRepository = invoiceRepository;
        this.patientRepository = patientRepository;
        this.paymentRepository = paymentRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void execute(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Factura no encontrada con ID: " + invoiceId));

        var patient = patientRepository.findById(invoice.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado"));

        if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
            String patientName = patient.getFirstName() + " " + patient.getLastName();
            List<String> itemsSummary = invoice.getItems().stream()
                    .map(i -> i.getDescription() + " — $" + i.getLineTotal())
                    .toList();

            var payments = paymentRepository.findByInvoiceId(invoiceId);
            String lastPaymentMethod = !payments.isEmpty() ? payments.get(payments.size() - 1).getPaymentMethod().name() : "N/A";

            emailSender.sendInvoiceReceiptNotification(
                    patient.getEmail(),
                    patientName,
                    invoice.getInvoiceNumber(),
                    invoice.getTotalAmount(),
                    invoice.getPaidAmount(),
                    invoice.getStatus().name(),
                    itemsSummary,
                    lastPaymentMethod
            );
        }
    }
}
