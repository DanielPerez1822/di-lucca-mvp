package com.odontosys.odontosys_api.domain.port.in.auth;

import com.odontosys.odontosys_api.application.auth.command.LoginCommand;
import com.odontosys.odontosys_api.application.auth.response.AuthResponse;

/**
 * Puerto primario — Caso de uso para iniciar sesión.
 */
public interface LoginUseCase {

    AuthResponse execute(LoginCommand command);
}
