package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Payment;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    List<Payment> findByInvoiceId(UUID invoiceId);
}
