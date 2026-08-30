package com.odontosys.odontosys_api.application.invoice;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.command.RegisterPaymentCommand;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.application.invoice.response.PaymentResponse;
import com.odontosys.odontosys_api.domain.exception.InvoiceNotFoundException;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.Payment;
import com.odontosys.odontosys_api.domain.port.in.invoice.RegisterPaymentUseCase;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;

public class RegisterPaymentService implements RegisterPaymentUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;
    private final PatientRepositoryPort patientRepository;
    private final EmailSenderPort emailSender;

    public RegisterPaymentService(InvoiceRepositoryPort invoiceRepository,
                                  PaymentRepositoryPort paymentRepository,
                                  PatientRepositoryPort patientRepository,
                                  EmailSenderPort emailSender) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.patientRepository = patientRepository;
        this.emailSender = emailSender;
    }

    @Override
    public InvoiceResponse execute(UUID invoiceId, RegisterPaymentCommand command) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Factura no encontrada con ID: " + invoiceId));

        Payment payment = Payment.create(invoiceId, command.amount(), command.paymentMethod(), command.referenceNumber(), command.notes());
        paymentRepository.save(payment);

        invoice.addPaymentAmount(command.amount());
        Invoice updatedInvoice = invoiceRepository.save(invoice);

        List<PaymentResponse> paymentResponses = paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(PaymentResponse::fromDomain)
                .toList();

        // Send email receipt automatically to patient when payment is registered
        patientRepository.findById(updatedInvoice.getPatientId()).ifPresent(patient -> {
            if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
                String patientName = patient.getFirstName() + " " + patient.getLastName();
                List<String> itemsSummary = updatedInvoice.getItems().stream()
                        .map(i -> i.getDescription() + " — $" + i.getLineTotal())
                        .toList();

                emailSender.sendInvoiceReceiptNotification(
                        patient.getEmail(),
                        patientName,
                        updatedInvoice.getInvoiceNumber(),
                        updatedInvoice.getTotalAmount(),
                        updatedInvoice.getPaidAmount(),
                        updatedInvoice.getStatus().name(),
                        itemsSummary,
                        command.paymentMethod().name()
                );
            }
        });

        return InvoiceResponse.fromDomain(updatedInvoice, paymentResponses);
    }
}
