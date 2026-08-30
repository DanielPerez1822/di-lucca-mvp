package com.odontosys.odontosys_api.application.patient;

import java.util.UUID;
import com.odontosys.odontosys_api.application.patient.command.UpdatePatientCommand;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;
import com.odontosys.odontosys_api.domain.exception.PatientAlreadyExistsException;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.domain.port.in.patient.UpdatePatientUseCase;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;

public class UpdatePatientService implements UpdatePatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public UpdatePatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse execute(UUID id, UpdatePatientCommand command) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));

        if (!patient.getDocumentNumber().equalsIgnoreCase(command.documentNumber())
                && patientRepository.existsByDocumentNumber(command.documentNumber())) {
            throw new PatientAlreadyExistsException("El número de documento " + command.documentNumber() + " ya pertenece a otro paciente");
        }

        patient.updateDetails(
                command.firstName(),
                command.lastName(),
                command.documentType(),
                command.documentNumber(),
                command.phone(),
                command.email(),
                command.dateOfBirth(),
                command.address()
        );

        Patient updated = patientRepository.save(patient);
        return PatientResponse.fromDomain(updated);
    }
}
