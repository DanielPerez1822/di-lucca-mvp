package com.odontosys.odontosys_api.domain.port.out;

/**
 * Puerto secundario — Contrato para codificación de contraseñas.
 * Desacopla el dominio del algoritmo concreto (BCrypt).
 */
public interface PasswordEncoderPort {

    /**
     * Codifica una contraseña en texto plano.
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con el hash.
     */
    boolean matches(String rawPassword, String encodedPassword);
}
