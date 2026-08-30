package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PatientJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.PatientPersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataPatientRepository;

@Component
public class JpaPatientRepositoryAdapter implements PatientRepositoryPort {

    private final SpringDataPatientRepository repository;
    private final PatientPersistenceMapper mapper;

    public JpaPatientRepositoryAdapter(SpringDataPatientRepository repository, PatientPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Patient save(Patient patient) {
        PatientJpaEntity entity = mapper.toJpa(patient);
        PatientJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Patient> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber).map(mapper::toDomain);
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public List<Patient> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
