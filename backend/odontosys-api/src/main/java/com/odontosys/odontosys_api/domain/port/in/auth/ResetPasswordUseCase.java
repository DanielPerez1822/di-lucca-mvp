package com.odontosys.odontosys_api.domain.port.in.auth;

import com.odontosys.odontosys_api.application.auth.command.ResetPasswordCommand;

/**
 * Puerto de Entrada — Restablecer contraseña con código de verificación.
 */
public interface ResetPasswordUseCase {

    void execute(ResetPasswordCommand command);
}
