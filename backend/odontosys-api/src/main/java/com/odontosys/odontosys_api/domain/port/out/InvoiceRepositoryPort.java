package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;

public interface InvoiceRepositoryPort {

    Invoice save(Invoice invoice);

    Optional<Invoice> findById(UUID id);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByMedicalRecordId(UUID medicalRecordId);

    List<Invoice> findByPatientId(UUID patientId);

    List<Invoice> findByFilters(UUID patientId, InvoiceStatus status);

    long count();
}
