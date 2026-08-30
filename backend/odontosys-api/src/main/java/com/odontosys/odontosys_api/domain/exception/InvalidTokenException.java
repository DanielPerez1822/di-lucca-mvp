package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando un refresh token es inválido, expirado o revocado.
 */
public class InvalidTokenException extends DomainException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
