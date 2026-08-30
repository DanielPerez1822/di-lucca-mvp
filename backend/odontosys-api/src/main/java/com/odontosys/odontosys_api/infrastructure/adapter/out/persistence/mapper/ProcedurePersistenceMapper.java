package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.ProcedureJpaEntity;

@Component
public class ProcedurePersistenceMapper {

    public Procedure toDomain(ProcedureJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Procedure.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public ProcedureJpaEntity toJpa(Procedure domain) {
        if (domain == null) {
            return null;
        }
        ProcedureJpaEntity entity = new ProcedureJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setDuration(domain.getDuration());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
