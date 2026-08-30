package com.odontosys.odontosys_api.application.procedure;

import java.util.UUID;
import com.odontosys.odontosys_api.domain.exception.ProcedureNotFoundException;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.port.in.procedure.DeleteProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;

public class DeleteProcedureService implements DeleteProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public DeleteProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public void execute(UUID id) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new ProcedureNotFoundException("Procedimiento no encontrado con ID: " + id));

        procedure.deactivate();
        procedureRepository.save(procedure);
    }
}
