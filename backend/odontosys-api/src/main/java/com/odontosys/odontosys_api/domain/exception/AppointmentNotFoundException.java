package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando no se encuentra la cita buscada.
 */
public class AppointmentNotFoundException extends DomainException {

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
