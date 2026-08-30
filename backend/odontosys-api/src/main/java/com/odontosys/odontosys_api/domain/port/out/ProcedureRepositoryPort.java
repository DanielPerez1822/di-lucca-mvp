package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Procedure;

public interface ProcedureRepositoryPort {

    Procedure save(Procedure procedure);

    Optional<Procedure> findById(UUID id);

    Optional<Procedure> findByName(String name);

    boolean existsByName(String name);

    List<Procedure> findAll();
}
