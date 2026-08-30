package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PatientJpaEntity;

public interface SpringDataPatientRepository extends JpaRepository<PatientJpaEntity, UUID> {

    Optional<PatientJpaEntity> findByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumber(String documentNumber);
}
