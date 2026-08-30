package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.InvoiceJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.InvoicePersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataInvoiceRepository;

@Component
public class JpaInvoiceRepositoryAdapter implements InvoiceRepositoryPort {

    private final SpringDataInvoiceRepository repository;
    private final InvoicePersistenceMapper mapper;

    public JpaInvoiceRepositoryAdapter(SpringDataInvoiceRepository repository, InvoicePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Invoice save(Invoice invoice) {
        InvoiceJpaEntity entity = mapper.toJpa(invoice);
        InvoiceJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return repository.findByInvoiceNumber(invoiceNumber).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findByMedicalRecordId(UUID medicalRecordId) {
        return repository.findByMedicalRecordId(medicalRecordId).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> findByPatientId(UUID patientId) {
        return repository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> findByFilters(UUID patientId, InvoiceStatus status) {
        return repository.findByFilters(patientId, status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}
