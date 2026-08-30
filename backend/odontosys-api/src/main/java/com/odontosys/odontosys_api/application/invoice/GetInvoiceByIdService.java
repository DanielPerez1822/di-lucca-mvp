package com.odontosys.odontosys_api.application.invoice;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.application.invoice.response.PaymentResponse;
import com.odontosys.odontosys_api.domain.exception.InvoiceNotFoundException;
import com.odontosys.odontosys_api.domain.port.in.invoice.GetInvoiceByIdUseCase;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;

public class GetInvoiceByIdService implements GetInvoiceByIdUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;

    public GetInvoiceByIdService(InvoiceRepositoryPort invoiceRepository, PaymentRepositoryPort paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public InvoiceResponse execute(UUID id) {
        var invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Factura no encontrada con ID: " + id));

        List<PaymentResponse> payments = paymentRepository.findByInvoiceId(id).stream()
                .map(PaymentResponse::fromDomain)
                .toList();

        return InvoiceResponse.fromDomain(invoice, payments);
    }
}
