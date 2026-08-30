package com.odontosys.odontosys_api.domain.port.in.patient;

import java.util.List;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;

public interface ListPatientsUseCase {

    List<PatientResponse> execute();
}
