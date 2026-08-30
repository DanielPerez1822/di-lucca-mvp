package com.odontosys.odontosys_api.domain.port.in.auth;

import java.util.UUID;

/**
 * Puerto primario — Caso de uso para cerrar sesión.
 */
public interface LogoutUseCase {

    void execute(UUID userId);
}
