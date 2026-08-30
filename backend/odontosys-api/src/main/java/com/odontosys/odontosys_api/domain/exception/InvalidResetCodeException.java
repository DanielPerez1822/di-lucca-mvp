package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando el código de restablecimiento es inválido, expirado o ya utilizado.
 */
public class InvalidResetCodeException extends DomainException {

    public InvalidResetCodeException(String message) {
        super(message);
    }
}
