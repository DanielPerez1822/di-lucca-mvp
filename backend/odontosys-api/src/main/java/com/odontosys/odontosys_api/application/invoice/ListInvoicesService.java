package com.odontosys.odontosys_api.application.invoice;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.application.invoice.response.PaymentResponse;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;
import com.odontosys.odontosys_api.domain.port.in.invoice.ListInvoicesUseCase;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;

public class ListInvoicesService implements ListInvoicesUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final PaymentRepositoryPort paymentRepository;

    public ListInvoicesService(InvoiceRepositoryPort invoiceRepository, PaymentRepositoryPort paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<InvoiceResponse> execute(UUID patientId, InvoiceStatus status) {
        return invoiceRepository.findByFilters(patientId, status).stream()
                .map(invoice -> {
                    List<PaymentResponse> payments = paymentRepository.findByInvoiceId(invoice.getId()).stream()
                            .map(PaymentResponse::fromDomain)
                            .toList();
                    return InvoiceResponse.fromDomain(invoice, payments);
                })
                .toList();
    }
}
