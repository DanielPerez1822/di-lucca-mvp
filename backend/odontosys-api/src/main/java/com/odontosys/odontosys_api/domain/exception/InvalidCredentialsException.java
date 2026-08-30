package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando las credenciales de login son inválidas.
 */
public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Credenciales inválidas");
    }
}
