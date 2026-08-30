package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.AvailabilitySlotJpaEntity;

@Component
public class AvailabilitySlotPersistenceMapper {

    public AvailabilitySlot toDomain(AvailabilitySlotJpaEntity entity) {
        if (entity == null) return null;
        return AvailabilitySlot.reconstitute(
                entity.getId(),
                entity.getDentistId(),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public AvailabilitySlotJpaEntity toJpa(AvailabilitySlot domain) {
        if (domain == null) return null;
        AvailabilitySlotJpaEntity entity = new AvailabilitySlotJpaEntity();
        entity.setId(domain.getId());
        entity.setDentistId(domain.getDentistId());
        entity.setDate(domain.getDate());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
