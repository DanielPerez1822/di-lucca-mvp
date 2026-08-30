package com.odontosys.odontosys_api.domain.port.in.medicalrecord;

import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.command.UpdateMedicalRecordCommand;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;

public interface UpdateMedicalRecordUseCase {

    MedicalRecordResponse execute(UUID id, UpdateMedicalRecordCommand command);
}
