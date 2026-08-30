package com.odontosys.odontosys_api.domain.port.in.auth;

import java.util.UUID;

import com.odontosys.odontosys_api.application.auth.response.UserResponse;

/**
 * Puerto primario — Caso de uso para obtener el usuario autenticado.
 */
public interface GetCurrentUserUseCase {

    UserResponse execute(UUID userId);
}
