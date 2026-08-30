package com.odontosys.odontosys_api.application.procedure;

import java.util.UUID;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;
import com.odontosys.odontosys_api.domain.exception.ProcedureNotFoundException;
import com.odontosys.odontosys_api.domain.port.in.procedure.GetProcedureByIdUseCase;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;

public class GetProcedureByIdService implements GetProcedureByIdUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public GetProcedureByIdService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public ProcedureResponse execute(UUID id) {
        return procedureRepository.findById(id)
                .map(ProcedureResponse::fromDomain)
                .orElseThrow(() -> new ProcedureNotFoundException("Procedimiento no encontrado con ID: " + id));
    }
}
