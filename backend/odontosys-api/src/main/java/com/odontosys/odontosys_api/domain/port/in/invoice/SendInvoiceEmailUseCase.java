package com.odontosys.odontosys_api.domain.port.in.invoice;

import java.util.UUID;

public interface SendInvoiceEmailUseCase {

    void execute(UUID invoiceId);
}
