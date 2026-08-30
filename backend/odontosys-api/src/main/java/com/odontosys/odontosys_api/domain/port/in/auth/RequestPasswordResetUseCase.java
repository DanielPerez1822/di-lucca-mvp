package com.odontosys.odontosys_api.domain.port.in.auth;

import com.odontosys.odontosys_api.application.auth.command.RequestPasswordResetCommand;

/**
 * Puerto de Entrada — Solicitar código de recuperación de contraseña.
 */
public interface RequestPasswordResetUseCase {

    void execute(RequestPasswordResetCommand command);
}
