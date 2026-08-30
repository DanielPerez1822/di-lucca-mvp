package com.odontosys.odontosys_api.domain.port.in.patient;

import java.util.UUID;
import com.odontosys.odontosys_api.application.patient.command.UpdatePatientCommand;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;

public interface UpdatePatientUseCase {

    PatientResponse execute(UUID id, UpdatePatientCommand command);
}
