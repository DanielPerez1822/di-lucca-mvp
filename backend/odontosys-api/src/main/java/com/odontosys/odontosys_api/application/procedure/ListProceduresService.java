package com.odontosys.odontosys_api.application.procedure;

import java.util.List;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;
import com.odontosys.odontosys_api.domain.port.in.procedure.ListProceduresUseCase;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;

public class ListProceduresService implements ListProceduresUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public ListProceduresService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public List<ProcedureResponse> execute() {
        return procedureRepository.findAll().stream()
                .map(ProcedureResponse::fromDomain)
                .toList();
    }
}
