package com.odontosys.odontosys_api.application.medicalrecord;

import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;
import com.odontosys.odontosys_api.domain.exception.MedicalRecordNotFoundException;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.GetMedicalRecordByIdUseCase;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;

public class GetMedicalRecordByIdService implements GetMedicalRecordByIdUseCase {

    private final MedicalRecordRepositoryPort medicalRecordRepository;

    public GetMedicalRecordByIdService(MedicalRecordRepositoryPort medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public MedicalRecordResponse execute(UUID id) {
        return medicalRecordRepository.findById(id)
                .map(MedicalRecordResponse::fromDomain)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Registro médico no encontrado con ID: " + id));
    }
}
