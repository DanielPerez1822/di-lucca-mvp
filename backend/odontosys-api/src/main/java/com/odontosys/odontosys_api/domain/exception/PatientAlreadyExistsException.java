package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando se intenta registrar un paciente con número de documento existente.
 */
public class PatientAlreadyExistsException extends DomainException {

    public PatientAlreadyExistsException(String message) {
        super(message);
    }
}
