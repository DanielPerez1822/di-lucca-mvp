package com.odontosys.odontosys_api.domain.port.in.medicalrecord;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;

public interface GetPatientMedicalHistoryUseCase {

    List<MedicalRecordResponse> execute(UUID patientId);
}
