package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.AppointmentJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.AppointmentPersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataAppointmentRepository;

@Component
public class JpaAppointmentRepositoryAdapter implements AppointmentRepositoryPort {

    private final SpringDataAppointmentRepository repository;
    private final AppointmentPersistenceMapper mapper;

    public JpaAppointmentRepositoryAdapter(SpringDataAppointmentRepository repository, AppointmentPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentJpaEntity entity = mapper.toJpa(appointment);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Appointment> findByFilters(UUID dentistId, UUID patientId, LocalDate startDate, LocalDate endDate, AppointmentStatus status) {
        return repository.findByFilters(dentistId, patientId, startDate, endDate, status).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
