package com.odontosys.odontosys_api.domain.port.in.invoice;

import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;

public interface GetInvoiceByIdUseCase {

    InvoiceResponse execute(UUID id);
}
