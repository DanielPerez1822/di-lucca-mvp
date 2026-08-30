package com.odontosys.odontosys_api.domain.port.out;

import java.util.UUID;

import com.odontosys.odontosys_api.domain.model.User;

/**
 * Puerto secundario — Contrato para generación y validación de tokens JWT.
 */
public interface TokenProviderPort {

    /**
     * Genera un access token JWT para el usuario.
     */
    String generateAccessToken(User user);

    /**
     * Genera un refresh token opaco (UUID).
     */
    String generateRefreshToken();

    /**
     * Valida un access token y retorna true si es válido.
     */
    boolean validateAccessToken(String token);

    /**
     * Extrae el ID del usuario desde un access token válido.
     */
    UUID getUserIdFromToken(String token);

    /**
     * Genera un hash del refresh token para almacenamiento seguro.
     */
    String hashRefreshToken(String rawToken);
}
