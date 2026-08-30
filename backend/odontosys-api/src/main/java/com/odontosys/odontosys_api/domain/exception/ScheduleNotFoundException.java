package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando no se encuentra un horario configurado.
 */
public class ScheduleNotFoundException extends DomainException {

    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
