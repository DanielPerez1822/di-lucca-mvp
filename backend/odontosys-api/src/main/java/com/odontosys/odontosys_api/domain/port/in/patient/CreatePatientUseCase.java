package com.odontosys.odontosys_api.domain.port.in.patient;

import com.odontosys.odontosys_api.application.patient.command.CreatePatientCommand;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;

public interface CreatePatientUseCase {

    PatientResponse execute(CreatePatientCommand command);
}
