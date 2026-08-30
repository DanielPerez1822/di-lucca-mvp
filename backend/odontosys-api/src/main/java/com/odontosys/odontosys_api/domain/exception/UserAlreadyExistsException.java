package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando se intenta crear un usuario con email o documento ya existente.
 */
public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
