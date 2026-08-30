package com.odontosys.odontosys_api.application.invoice;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.application.invoice.response.PaymentResponse;
import com.odontosys.odontosys_api.domain.exception.InvoiceNotFoundException;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;
import com.odontosys.odontosys_api.domain.port.in.invoice.SendInvoiceEmailUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.UpdateInvoiceUseCase;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;

public class UpdateInvoiceService implements UpdateInvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;
    private final SendInvoiceEmailUseCase sendInvoiceEmailUseCase;

    public UpdateInvoiceService(InvoiceRepositoryPort invoiceRepository,
                                PaymentRepositoryPort paymentRepository,
                                SendInvoiceEmailUseCase sendInvoiceEmailUseCase) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.sendInvoiceEmailUseCase = sendInvoiceEmailUseCase;
    }

    @Override
    @Transactional
    public InvoiceResponse execute(UUID invoiceId, InvoiceStatus status, BigDecimal paidAmount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Factura no encontrada con ID: " + invoiceId));

        invoice.updateStatusAndPaidAmount(status, paidAmount);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Envío automático del comprobante HTML actualizado al correo del paciente
        try {
            sendInvoiceEmailUseCase.execute(invoiceId);
        } catch (Exception e) {
            // Se captura para no revertir la actualización de la factura si el correo falla temporalmente
        }

        List<PaymentResponse> payments = paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(PaymentResponse::fromDomain)
                .toList();

        return InvoiceResponse.fromDomain(savedInvoice, payments);
    }
}
