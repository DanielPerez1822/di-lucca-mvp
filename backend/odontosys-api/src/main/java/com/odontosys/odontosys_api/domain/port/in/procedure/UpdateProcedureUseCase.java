package com.odontosys.odontosys_api.domain.port.in.procedure;

import java.util.UUID;
import com.odontosys.odontosys_api.application.procedure.command.UpdateProcedureCommand;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;

public interface UpdateProcedureUseCase {

    ProcedureResponse execute(UUID id, UpdateProcedureCommand command);
}
