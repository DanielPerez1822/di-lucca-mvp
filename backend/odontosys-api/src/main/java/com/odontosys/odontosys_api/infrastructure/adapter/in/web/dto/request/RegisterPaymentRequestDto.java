package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import com.odontosys.odontosys_api.domain.model.PaymentMethod;

public record RegisterPaymentRequestDto(
        @NotNull(message = "El monto del pago es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto del pago debe ser mayor a 0")
        BigDecimal amount,

        @NotNull(message = "El método de pago es obligatorio (CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, NEQUI_DAVIPLATA, OTHER)")
        PaymentMethod paymentMethod,

        String referenceNumber,

        String notes
) {
}
