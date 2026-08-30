package com.odontosys.odontosys_api.domain.port.in.patient;

import java.util.UUID;

public interface DeletePatientUseCase {

    void execute(UUID id);
}
