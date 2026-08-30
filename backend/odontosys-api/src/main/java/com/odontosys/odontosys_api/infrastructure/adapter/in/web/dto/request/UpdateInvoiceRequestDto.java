package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.math.BigDecimal;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;
import jakarta.validation.constraints.DecimalMin;

public record UpdateInvoiceRequestDto(
        InvoiceStatus status,
        @DecimalMin(value = "0.0", message = "El monto pagado no puede ser negativo")
        BigDecimal paidAmount
) {
}
