package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando no se encuentra un registro médico clínico.
 */
public class MedicalRecordNotFoundException extends DomainException {

    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
}
