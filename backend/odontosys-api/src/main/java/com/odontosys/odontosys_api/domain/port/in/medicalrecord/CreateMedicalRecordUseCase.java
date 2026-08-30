package com.odontosys.odontosys_api.domain.port.in.medicalrecord;

import com.odontosys.odontosys_api.application.medicalrecord.command.CreateMedicalRecordCommand;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;

public interface CreateMedicalRecordUseCase {

    MedicalRecordResponse execute(CreateMedicalRecordCommand command);
}
