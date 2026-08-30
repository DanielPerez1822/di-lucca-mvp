package com.odontosys.odontosys_api.domain.port.in.invoice;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;

public interface ListInvoicesUseCase {

    List<InvoiceResponse> execute(UUID patientId, InvoiceStatus status);
}
