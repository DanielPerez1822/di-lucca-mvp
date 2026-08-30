package com.odontosys.odontosys_api.application.procedure;

import java.util.UUID;
import com.odontosys.odontosys_api.application.procedure.command.UpdateProcedureCommand;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;
import com.odontosys.odontosys_api.domain.exception.ProcedureAlreadyExistsException;
import com.odontosys.odontosys_api.domain.exception.ProcedureNotFoundException;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.port.in.procedure.UpdateProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;

public class UpdateProcedureService implements UpdateProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public UpdateProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public ProcedureResponse execute(UUID id, UpdateProcedureCommand command) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new ProcedureNotFoundException("Procedimiento no encontrado con ID: " + id));

        if (!procedure.getName().equalsIgnoreCase(command.name())
                && procedureRepository.existsByName(command.name())) {
            throw new ProcedureAlreadyExistsException("Ya existe otro procedimiento con el nombre: " + command.name());
        }

        procedure.update(command.name(), command.description(), command.price(), command.duration());

        Procedure updated = procedureRepository.save(procedure);
        return ProcedureResponse.fromDomain(updated);
    }
}
