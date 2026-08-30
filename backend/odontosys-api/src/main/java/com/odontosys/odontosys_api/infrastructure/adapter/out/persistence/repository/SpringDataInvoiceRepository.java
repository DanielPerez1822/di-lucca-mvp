package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.InvoiceJpaEntity;

public interface SpringDataInvoiceRepository extends JpaRepository<InvoiceJpaEntity, UUID> {

    Optional<InvoiceJpaEntity> findByInvoiceNumber(String invoiceNumber);

    Optional<InvoiceJpaEntity> findByMedicalRecordId(UUID medicalRecordId);

    List<InvoiceJpaEntity> findByPatientIdOrderByCreatedAtDesc(UUID patientId);

    @Query("SELECT i FROM InvoiceJpaEntity i WHERE " +
           "(:patientId IS NULL OR i.patientId = :patientId) AND " +
           "(:status IS NULL OR i.status = :status) " +
           "ORDER BY i.createdAt DESC")
    List<InvoiceJpaEntity> findByFilters(@Param("patientId") UUID patientId, @Param("status") InvoiceStatus status);
}
