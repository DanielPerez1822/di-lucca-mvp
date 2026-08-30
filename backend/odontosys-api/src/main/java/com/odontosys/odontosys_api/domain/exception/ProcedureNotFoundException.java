package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando el procedimiento dental buscado no existe.
 */
public class ProcedureNotFoundException extends DomainException {

    public ProcedureNotFoundException(String message) {
        super(message);
    }
}
