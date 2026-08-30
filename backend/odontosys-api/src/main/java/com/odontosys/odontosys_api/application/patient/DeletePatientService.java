package com.odontosys.odontosys_api.application.patient;

import java.util.UUID;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.domain.port.in.patient.DeletePatientUseCase;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;

public class DeletePatientService implements DeletePatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public DeletePatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void execute(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));

        patient.deactivate();
        patientRepository.save(patient);
    }
}
