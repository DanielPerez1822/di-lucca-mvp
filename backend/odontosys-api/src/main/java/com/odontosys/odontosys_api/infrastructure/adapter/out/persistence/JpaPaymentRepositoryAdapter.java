package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Payment;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PaymentJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataPaymentRepository;

@Component
public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final SpringDataPaymentRepository repository;
    private final PaymentPersistenceMapper mapper;

    public JpaPaymentRepositoryAdapter(SpringDataPaymentRepository repository, PaymentPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity entity = mapper.toJpa(payment);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Payment> findByInvoiceId(UUID invoiceId) {
        return repository.findByInvoiceIdOrderByPaymentDateAsc(invoiceId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
