package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.odontosys.odontosys_api.domain.model.User;

/**
 * Puerto secundario — Contrato de persistencia para usuarios.
 * La implementación concreta vive en infrastructure.
 */
public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDocumentNumber(String documentNumber);

    List<User> findAll();
}
