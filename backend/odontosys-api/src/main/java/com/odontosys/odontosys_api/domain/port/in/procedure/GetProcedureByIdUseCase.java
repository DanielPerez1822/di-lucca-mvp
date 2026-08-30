package com.odontosys.odontosys_api.domain.port.in.procedure;

import java.util.UUID;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;

public interface GetProcedureByIdUseCase {

    ProcedureResponse execute(UUID id);
}
