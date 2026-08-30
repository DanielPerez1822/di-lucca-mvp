package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando un usuario desactivado intenta iniciar sesión.
 */
public class UserDisabledException extends DomainException {

    public UserDisabledException() {
        super("La cuenta está desactivada");
    }
}
