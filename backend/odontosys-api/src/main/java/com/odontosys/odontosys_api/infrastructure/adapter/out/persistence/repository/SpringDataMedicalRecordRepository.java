package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.MedicalRecordJpaEntity;

public interface SpringDataMedicalRecordRepository extends JpaRepository<MedicalRecordJpaEntity, UUID> {

    List<MedicalRecordJpaEntity> findByPatientIdOrderByCreatedAtDesc(UUID patientId);

    Optional<MedicalRecordJpaEntity> findByAppointmentId(UUID appointmentId);
}
