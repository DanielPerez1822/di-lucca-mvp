package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.InvoiceItem;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.InvoiceItemJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.InvoiceJpaEntity;

@Component
public class InvoicePersistenceMapper {

    public Invoice toDomain(InvoiceJpaEntity entity) {
        if (entity == null) return null;

        List<InvoiceItem> domainItems = entity.getItems().stream()
                .map(item -> InvoiceItem.reconstitute(
                        item.getId(),
                        item.getProcedureId(),
                        item.getDescription(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()
                )).toList();

        return Invoice.reconstitute(
                entity.getId(),
                entity.getInvoiceNumber(),
                entity.getPatientId(),
                entity.getMedicalRecordId(),
                entity.getIssueDate(),
                entity.getSubtotal(),
                entity.getTaxAmount(),
                entity.getDiscountAmount(),
                entity.getTotalAmount(),
                entity.getPaidAmount(),
                entity.getStatus(),
                domainItems,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public InvoiceJpaEntity toJpa(Invoice domain) {
        if (domain == null) return null;

        InvoiceJpaEntity entity = new InvoiceJpaEntity();
        entity.setId(domain.getId());
        entity.setInvoiceNumber(domain.getInvoiceNumber());
        entity.setPatientId(domain.getPatientId());
        entity.setMedicalRecordId(domain.getMedicalRecordId());
        entity.setIssueDate(domain.getIssueDate());
        entity.setSubtotal(domain.getSubtotal());
        entity.setTaxAmount(domain.getTaxAmount());
        entity.setDiscountAmount(domain.getDiscountAmount());
        entity.setTotalAmount(domain.getTotalAmount());
        entity.setPaidAmount(domain.getPaidAmount());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        for (InvoiceItem itemDomain : domain.getItems()) {
            InvoiceItemJpaEntity itemJpa = new InvoiceItemJpaEntity();
            itemJpa.setId(itemDomain.getId());
            itemJpa.setProcedureId(itemDomain.getProcedureId());
            itemJpa.setDescription(itemDomain.getDescription());
            itemJpa.setQuantity(itemDomain.getQuantity());
            itemJpa.setUnitPrice(itemDomain.getUnitPrice());
            itemJpa.setLineTotal(itemDomain.getLineTotal());
            entity.addItem(itemJpa);
        }

        return entity;
    }
}
