package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;

public interface MedicalRecordRepositoryPort {

    MedicalRecord save(MedicalRecord record);

    Optional<MedicalRecord> findById(UUID id);

    List<MedicalRecord> findByPatientId(UUID patientId);

    Optional<MedicalRecord> findByAppointmentId(UUID appointmentId);
}
