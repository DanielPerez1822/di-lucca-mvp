package com.odontosys.odontosys_api.domain.port.in.procedure;

import java.util.UUID;

public interface DeleteProcedureUseCase {

    void execute(UUID id);
}
