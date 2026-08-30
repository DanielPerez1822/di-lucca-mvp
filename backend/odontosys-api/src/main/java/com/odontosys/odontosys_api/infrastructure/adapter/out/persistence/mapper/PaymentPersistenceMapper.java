package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Payment;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PaymentJpaEntity;

@Component
public class PaymentPersistenceMapper {

    public Payment toDomain(PaymentJpaEntity entity) {
        if (entity == null) return null;
        return Payment.reconstitute(
                entity.getId(),
                entity.getInvoiceId(),
                entity.getAmount(),
                entity.getPaymentMethod(),
                entity.getReferenceNumber(),
                entity.getNotes(),
                entity.getPaymentDate()
        );
    }

    public PaymentJpaEntity toJpa(Payment domain) {
        if (domain == null) return null;
        PaymentJpaEntity entity = new PaymentJpaEntity();
        entity.setId(domain.getId());
        entity.setInvoiceId(domain.getInvoiceId());
        entity.setAmount(domain.getAmount());
        entity.setPaymentMethod(domain.getPaymentMethod());
        entity.setReferenceNumber(domain.getReferenceNumber());
        entity.setNotes(domain.getNotes());
        entity.setPaymentDate(domain.getPaymentDate());
        return entity;
    }
}
