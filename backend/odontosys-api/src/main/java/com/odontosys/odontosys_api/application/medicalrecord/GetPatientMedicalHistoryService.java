package com.odontosys.odontosys_api.application.medicalrecord;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.GetPatientMedicalHistoryUseCase;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;

public class GetPatientMedicalHistoryService implements GetPatientMedicalHistoryUseCase {

    private final MedicalRecordRepositoryPort medicalRecordRepository;
    private final PatientRepositoryPort patientRepository;

    public GetPatientMedicalHistoryService(MedicalRecordRepositoryPort medicalRecordRepository,
                                          PatientRepositoryPort patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<MedicalRecordResponse> execute(UUID patientId) {
        if (patientRepository.findById(patientId).isEmpty()) {
            throw new PatientNotFoundException("Paciente no encontrado con ID: " + patientId);
        }

        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(MedicalRecordResponse::fromDomain)
                .toList();
    }
}
