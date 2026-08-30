package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.ProcedureJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.ProcedurePersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataProcedureRepository;

@Component
public class JpaProcedureRepositoryAdapter implements ProcedureRepositoryPort {

    private final SpringDataProcedureRepository repository;
    private final ProcedurePersistenceMapper mapper;

    public JpaProcedureRepositoryAdapter(SpringDataProcedureRepository repository, ProcedurePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Procedure save(Procedure procedure) {
        ProcedureJpaEntity entity = mapper.toJpa(procedure);
        ProcedureJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Procedure> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Procedure> findByName(String name) {
        return repository.findByNameIgnoreCase(name).map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public List<Procedure> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
