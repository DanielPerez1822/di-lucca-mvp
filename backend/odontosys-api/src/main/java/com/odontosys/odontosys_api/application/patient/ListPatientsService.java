package com.odontosys.odontosys_api.application.patient;

import java.util.List;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;
import com.odontosys.odontosys_api.domain.port.in.patient.ListPatientsUseCase;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;

public class ListPatientsService implements ListPatientsUseCase {

    private final PatientRepositoryPort patientRepository;

    public ListPatientsService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientResponse> execute() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::fromDomain)
                .toList();
    }
}
