package com.odontosys.odontosys_api.domain.port.in.auth;

import com.odontosys.odontosys_api.application.auth.command.RefreshTokenCommand;
import com.odontosys.odontosys_api.application.auth.response.TokenResponse;

/**
 * Puerto primario — Caso de uso para renovar tokens JWT.
 */
public interface RefreshTokenUseCase {

    TokenResponse execute(RefreshTokenCommand command);
}
