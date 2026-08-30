package com.odontosys.odontosys_api.application.patient;

import java.util.UUID;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.port.in.patient.GetPatientByIdUseCase;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;

public class GetPatientByIdService implements GetPatientByIdUseCase {

    private final PatientRepositoryPort patientRepository;

    public GetPatientByIdService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse execute(UUID id) {
        return patientRepository.findById(id)
                .map(PatientResponse::fromDomain)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));
    }
}
