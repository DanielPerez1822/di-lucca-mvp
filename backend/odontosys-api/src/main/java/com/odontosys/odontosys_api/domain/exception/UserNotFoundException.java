package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando el usuario buscado no existe en el sistema.
 */
public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
