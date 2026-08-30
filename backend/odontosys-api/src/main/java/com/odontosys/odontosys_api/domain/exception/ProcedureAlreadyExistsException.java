package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando se intenta crear un procedimiento con un nombre ya existente.
 */
public class ProcedureAlreadyExistsException extends DomainException {

    public ProcedureAlreadyExistsException(String message) {
        super(message);
    }
}
