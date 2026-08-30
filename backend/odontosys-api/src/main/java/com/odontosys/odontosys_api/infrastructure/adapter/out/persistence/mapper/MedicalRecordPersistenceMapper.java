package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;
import com.odontosys.odontosys_api.domain.model.MedicalRecordProcedureItem;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.MedicalRecordJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.MedicalRecordProcedureJpaEntity;

@Component
public class MedicalRecordPersistenceMapper {

    public MedicalRecord toDomain(MedicalRecordJpaEntity entity) {
        if (entity == null) return null;

        List<MedicalRecordProcedureItem> domainItems = entity.getItems().stream()
                .map(item -> MedicalRecordProcedureItem.reconstitute(
                        item.getId(),
                        item.getProcedureId(),
                        item.getProcedureName(),
                        item.getAppliedPrice(),
                        item.getToothNumber(),
                        item.getNotes()
                )).toList();

        return MedicalRecord.reconstitute(
                entity.getId(),
                entity.getPatientId(),
                entity.getDentistId(),
                entity.getAppointmentId(),
                entity.getDiagnosis(),
                entity.getNotes(),
                domainItems,
                entity.getTotalAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public MedicalRecordJpaEntity toJpa(MedicalRecord domain) {
        if (domain == null) return null;

        MedicalRecordJpaEntity entity = new MedicalRecordJpaEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId());
        entity.setDentistId(domain.getDentistId());
        entity.setAppointmentId(domain.getAppointmentId());
        entity.setDiagnosis(domain.getDiagnosis());
        entity.setNotes(domain.getNotes());
        entity.setTotalAmount(domain.getTotalAmount());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        for (MedicalRecordProcedureItem itemDomain : domain.getItems()) {
            MedicalRecordProcedureJpaEntity itemJpa = new MedicalRecordProcedureJpaEntity();
            itemJpa.setId(itemDomain.getId());
            itemJpa.setProcedureId(itemDomain.getProcedureId());
            itemJpa.setProcedureName(itemDomain.getProcedureName());
            itemJpa.setAppliedPrice(itemDomain.getAppliedPrice());
            itemJpa.setToothNumber(itemDomain.getToothNumber());
            itemJpa.setNotes(itemDomain.getNotes());
            entity.addItem(itemJpa);
        }

        return entity;
    }
}
