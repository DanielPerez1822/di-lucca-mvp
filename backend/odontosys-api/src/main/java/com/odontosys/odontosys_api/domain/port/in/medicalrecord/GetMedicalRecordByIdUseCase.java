package com.odontosys.odontosys_api.domain.port.in.medicalrecord;

import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;

public interface GetMedicalRecordByIdUseCase {

    MedicalRecordResponse execute(UUID id);
}
