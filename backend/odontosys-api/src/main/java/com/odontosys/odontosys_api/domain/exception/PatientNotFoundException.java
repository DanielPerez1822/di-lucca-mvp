package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando el paciente buscado no existe.
 */
public class PatientNotFoundException extends DomainException {

    public PatientNotFoundException(String message) {
        super(message);
    }
}
