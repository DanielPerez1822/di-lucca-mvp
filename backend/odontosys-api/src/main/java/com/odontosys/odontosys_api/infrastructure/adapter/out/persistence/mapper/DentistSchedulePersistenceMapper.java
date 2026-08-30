package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.DentistScheduleJpaEntity;

@Component
public class DentistSchedulePersistenceMapper {

    public DentistSchedule toDomain(DentistScheduleJpaEntity entity) {
        if (entity == null) return null;
        return DentistSchedule.reconstitute(
                entity.getId(),
                entity.getDentistId(),
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getSlotDurationMinutes(),
                entity.isHasBreak(),
                entity.getBreakStartTime(),
                entity.getBreakEndTime(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public DentistScheduleJpaEntity toJpa(DentistSchedule domain) {
        if (domain == null) return null;
        DentistScheduleJpaEntity entity = new DentistScheduleJpaEntity();
        entity.setId(domain.getId());
        entity.setDentistId(domain.getDentistId());
        entity.setDayOfWeek(domain.getDayOfWeek());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setSlotDurationMinutes(domain.getSlotDurationMinutes());
        entity.setHasBreak(domain.isHasBreak());
        entity.setBreakStartTime(domain.getBreakStartTime());
        entity.setBreakEndTime(domain.getBreakEndTime());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
