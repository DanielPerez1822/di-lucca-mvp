package com.odontosys.odontosys_api.domain.port.in.invoice;

import java.math.BigDecimal;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;

public interface UpdateInvoiceUseCase {
    InvoiceResponse execute(UUID invoiceId, InvoiceStatus status, BigDecimal paidAmount);
}
