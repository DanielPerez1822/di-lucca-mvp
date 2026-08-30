package com.odontosys.odontosys_api.application.procedure;

import com.odontosys.odontosys_api.application.procedure.command.CreateProcedureCommand;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;
import com.odontosys.odontosys_api.domain.exception.ProcedureAlreadyExistsException;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.port.in.procedure.CreateProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;

public class CreateProcedureService implements CreateProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public CreateProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public ProcedureResponse execute(CreateProcedureCommand command) {
        if (procedureRepository.existsByName(command.name())) {
            throw new ProcedureAlreadyExistsException("Ya existe un procedimiento con el nombre: " + command.name());
        }

        Procedure procedure = Procedure.create(
                command.name(),
                command.description(),
                command.price(),
                command.duration()
        );

        Procedure saved = procedureRepository.save(procedure);
        return ProcedureResponse.fromDomain(saved);
    }
}
