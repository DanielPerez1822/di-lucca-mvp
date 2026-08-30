package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;
import com.odontosys.odontosys_api.domain.port.out.DentistScheduleRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.DentistScheduleJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.DentistSchedulePersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataDentistScheduleRepository;

@Component
public class JpaDentistScheduleRepositoryAdapter implements DentistScheduleRepositoryPort {

    private final SpringDataDentistScheduleRepository repository;
    private final DentistSchedulePersistenceMapper mapper;

    public JpaDentistScheduleRepositoryAdapter(SpringDataDentistScheduleRepository repository, DentistSchedulePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DentistSchedule save(DentistSchedule schedule) {
        DentistScheduleJpaEntity entity = mapper.toJpa(schedule);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    public List<DentistSchedule> saveAll(List<DentistSchedule> schedules) {
        List<DentistScheduleJpaEntity> entities = schedules.stream().map(mapper::toJpa).toList();
        return repository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<DentistSchedule> findByDentistId(UUID dentistId) {
        return repository.findByDentistId(dentistId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteByDentistId(UUID dentistId) {
        repository.deleteByDentistId(dentistId);
    }
}
