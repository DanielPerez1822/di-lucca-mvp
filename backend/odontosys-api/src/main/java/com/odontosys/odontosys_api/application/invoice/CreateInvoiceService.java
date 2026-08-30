package com.odontosys.odontosys_api.application.invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.domain.exception.MedicalRecordNotFoundException;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.InvoiceItem;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;
import com.odontosys.odontosys_api.domain.port.in.invoice.CreateInvoiceFromMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;

public class CreateInvoiceService implements CreateInvoiceFromMedicalRecordUseCase {

    private final InvoiceRepositoryPort invoiceRepository;
    private final MedicalRecordRepositoryPort medicalRecordRepository;

    public CreateInvoiceService(InvoiceRepositoryPort invoiceRepository, MedicalRecordRepositoryPort medicalRecordRepository) {
        this.invoiceRepository = invoiceRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public InvoiceResponse execute(UUID medicalRecordId) {
        MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Registro médico no encontrado con ID: " + medicalRecordId));

        // Check if invoice already exists for this medical record
        var existingInvoice = invoiceRepository.findByMedicalRecordId(medicalRecordId);
        if (existingInvoice.isPresent()) {
            return InvoiceResponse.fromDomain(existingInvoice.get(), List.of());
        }

        // Generate consecutive invoice number (e.g. INV-00001)
        long nextSeq = invoiceRepository.count() + 1;
        String invoiceNumber = String.format("INV-%05d", nextSeq);

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (var item : record.getItems()) {
            String desc = item.getProcedureName() + (item.getToothNumber() != null ? " (Diente " + item.getToothNumber() + ")" : "");
            InvoiceItem invoiceItem = InvoiceItem.create(item.getProcedureId(), desc, 1, item.getAppliedPrice());
            invoiceItems.add(invoiceItem);
        }

        Invoice invoice = Invoice.create(invoiceNumber, record.getPatientId(), medicalRecordId, invoiceItems, null, null);
        Invoice saved = invoiceRepository.save(invoice);

        return InvoiceResponse.fromDomain(saved, List.of());
    }
}
