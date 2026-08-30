package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Patient;

public interface PatientRepositoryPort {

    Patient save(Patient patient);

    Optional<Patient> findById(UUID id);

    Optional<Patient> findByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumber(String documentNumber);

    List<Patient> findAll();
}
