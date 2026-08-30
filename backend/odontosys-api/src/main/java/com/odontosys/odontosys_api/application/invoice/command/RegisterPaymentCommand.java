package com.odontosys.odontosys_api.application.invoice.command;

import java.math.BigDecimal;
import com.odontosys.odontosys_api.domain.model.PaymentMethod;

public record RegisterPaymentCommand(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String referenceNumber,
        String notes
) {
}
