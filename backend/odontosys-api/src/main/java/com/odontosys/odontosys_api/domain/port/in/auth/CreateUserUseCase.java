package com.odontosys.odontosys_api.domain.port.in.auth;

import com.odontosys.odontosys_api.application.auth.command.CreateUserCommand;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;

/**
 * Puerto primario — Caso de uso para crear un usuario (solo ADMIN).
 */
public interface CreateUserUseCase {

    UserResponse execute(CreateUserCommand command);
}
