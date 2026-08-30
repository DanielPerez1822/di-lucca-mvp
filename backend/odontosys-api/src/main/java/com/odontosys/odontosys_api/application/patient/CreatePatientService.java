package com.odontosys.odontosys_api.application.patient;

import com.odontosys.odontosys_api.application.patient.command.CreatePatientCommand;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;
import com.odontosys.odontosys_api.domain.exception.PatientAlreadyExistsException;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.domain.port.in.patient.CreatePatientUseCase;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;

public class CreatePatientService implements CreatePatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public CreatePatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse execute(CreatePatientCommand command) {
        if (patientRepository.existsByDocumentNumber(command.documentNumber())) {
            throw new PatientAlreadyExistsException("El paciente con número de documento " + command.documentNumber() + " ya está registrado");
        }

        Patient patient = Patient.create(
                command.firstName(),
                command.lastName(),
                command.documentType(),
                command.documentNumber(),
                command.phone(),
                command.email(),
                command.dateOfBirth(),
                command.address()
        );

        Patient saved = patientRepository.save(patient);
        return PatientResponse.fromDomain(saved);
    }
}
