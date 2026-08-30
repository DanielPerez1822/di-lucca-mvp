package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.MedicalRecordJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.MedicalRecordPersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataMedicalRecordRepository;

@Component
public class JpaMedicalRecordRepositoryAdapter implements MedicalRecordRepositoryPort {

    private final SpringDataMedicalRecordRepository repository;
    private final MedicalRecordPersistenceMapper mapper;

    public JpaMedicalRecordRepositoryAdapter(SpringDataMedicalRecordRepository repository, MedicalRecordPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public MedicalRecord save(MedicalRecord record) {
        MedicalRecordJpaEntity entity = mapper.toJpa(record);
        MedicalRecordJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalRecord> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecord> findByPatientId(UUID patientId) {
        return repository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalRecord> findByAppointmentId(UUID appointmentId) {
        return repository.findByAppointmentId(appointmentId).map(mapper::toDomain);
    }
}
