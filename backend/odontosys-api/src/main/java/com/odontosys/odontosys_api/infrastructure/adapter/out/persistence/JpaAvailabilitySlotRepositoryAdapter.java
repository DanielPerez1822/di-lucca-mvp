package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.SlotStatus;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.AvailabilitySlotJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.AvailabilitySlotPersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataAvailabilitySlotRepository;

@Component
public class JpaAvailabilitySlotRepositoryAdapter implements AvailabilitySlotRepositoryPort {

    private final SpringDataAvailabilitySlotRepository repository;
    private final AvailabilitySlotPersistenceMapper mapper;

    public JpaAvailabilitySlotRepositoryAdapter(SpringDataAvailabilitySlotRepository repository, AvailabilitySlotPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AvailabilitySlot save(AvailabilitySlot slot) {
        AvailabilitySlotJpaEntity entity = mapper.toJpa(slot);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    public List<AvailabilitySlot> saveAll(List<AvailabilitySlot> slots) {
        List<AvailabilitySlotJpaEntity> entities = slots.stream().map(mapper::toJpa).toList();
        return repository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<AvailabilitySlot> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AvailabilitySlot> findByDentistIdAndDateBetween(UUID dentistId, LocalDate startDate, LocalDate endDate) {
        return repository.findByDentistIdAndDateBetweenOrderByDateAscStartTimeAsc(dentistId, startDate, endDate)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<AvailabilitySlot> findByDentistIdAndDateBetweenAndStatus(UUID dentistId, LocalDate startDate, LocalDate endDate, SlotStatus status) {
        return repository.findByDentistIdAndDateBetweenAndStatusOrderByDateAscStartTimeAsc(dentistId, startDate, endDate, status)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByDentistIdAndDateAndStartTime(UUID dentistId, LocalDate date, LocalTime startTime) {
        return repository.existsByDentistIdAndDateAndStartTime(dentistId, date, startTime);
    }
}
