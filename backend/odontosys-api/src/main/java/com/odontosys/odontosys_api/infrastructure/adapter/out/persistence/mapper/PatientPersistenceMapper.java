package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PatientJpaEntity;

@Component
public class PatientPersistenceMapper {

    public Patient toDomain(PatientJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Patient.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDocumentType(),
                entity.getDocumentNumber(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getDateOfBirth(),
                entity.getAddress(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public PatientJpaEntity toJpa(Patient domain) {
        if (domain == null) {
            return null;
        }
        PatientJpaEntity entity = new PatientJpaEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setDocumentType(domain.getDocumentType());
        entity.setDocumentNumber(domain.getDocumentNumber());
        entity.setPhone(domain.getPhone());
        entity.setEmail(domain.getEmail());
        entity.setDateOfBirth(domain.getDateOfBirth());
        entity.setAddress(domain.getAddress());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
