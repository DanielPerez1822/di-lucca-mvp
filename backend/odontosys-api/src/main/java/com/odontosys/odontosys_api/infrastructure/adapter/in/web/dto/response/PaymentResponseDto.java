package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.PaymentResponse;
import com.odontosys.odontosys_api.domain.model.PaymentMethod;

public record PaymentResponseDto(
        UUID id,
        UUID invoiceId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String referenceNumber,
        String notes,
        Instant paymentDate
) {
    public static PaymentResponseDto fromApplication(PaymentResponse response) {
        return new PaymentResponseDto(
                response.id(),
                response.invoiceId(),
                response.amount(),
                response.paymentMethod(),
                response.referenceNumber(),
                response.notes(),
                response.paymentDate()
        );
    }
}
