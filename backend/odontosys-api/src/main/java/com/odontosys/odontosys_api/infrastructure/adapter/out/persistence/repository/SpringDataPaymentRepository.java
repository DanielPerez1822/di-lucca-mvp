package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PaymentJpaEntity;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentJpaEntity, UUID> {

    List<PaymentJpaEntity> findByInvoiceIdOrderByPaymentDateAsc(UUID invoiceId);
}
