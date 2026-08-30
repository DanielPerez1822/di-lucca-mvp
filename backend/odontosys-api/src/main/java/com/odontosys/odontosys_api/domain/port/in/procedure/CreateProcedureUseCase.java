package com.odontosys.odontosys_api.domain.port.in.procedure;

import com.odontosys.odontosys_api.application.procedure.command.CreateProcedureCommand;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;

public interface CreateProcedureUseCase {

    ProcedureResponse execute(CreateProcedureCommand command);
}
