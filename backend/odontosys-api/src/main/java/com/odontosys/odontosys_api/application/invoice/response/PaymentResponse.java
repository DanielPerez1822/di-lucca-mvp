package com.odontosys.odontosys_api.application.invoice.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Payment;
import com.odontosys.odontosys_api.domain.model.PaymentMethod;

public record PaymentResponse(
        UUID id,
        UUID invoiceId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String referenceNumber,
        String notes,
        Instant paymentDate
) {
    public static PaymentResponse fromDomain(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getInvoiceId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getReferenceNumber(),
                payment.getNotes(),
                payment.getPaymentDate()
        );
    }
}
