package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.AppointmentJpaEntity;

@Component
public class AppointmentPersistenceMapper {

    public Appointment toDomain(AppointmentJpaEntity entity) {
        if (entity == null) return null;
        return Appointment.reconstitute(
                entity.getId(),
                entity.getPatientId(),
                entity.getDentistId(),
                entity.getSlotId(),
                entity.getAppointmentDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getReason(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public AppointmentJpaEntity toJpa(Appointment domain) {
        if (domain == null) return null;
        AppointmentJpaEntity entity = new AppointmentJpaEntity();
        entity.setId(domain.getId());
        entity.setPatientId(domain.getPatientId());
        entity.setDentistId(domain.getDentistId());
        entity.setSlotId(domain.getSlotId());
        entity.setAppointmentDate(domain.getAppointmentDate());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setReason(domain.getReason());
        entity.setStatus(domain.getStatus());
        entity.setNotes(domain.getNotes());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
