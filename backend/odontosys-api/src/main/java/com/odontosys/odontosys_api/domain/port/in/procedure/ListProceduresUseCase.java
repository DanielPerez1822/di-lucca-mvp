package com.odontosys.odontosys_api.domain.port.in.procedure;

import java.util.List;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;

public interface ListProceduresUseCase {

    List<ProcedureResponse> execute();
}
