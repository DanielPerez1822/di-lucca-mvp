package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando el slot de disponibilidad no está disponible para reserva.
 */
public class SlotNotAvailableException extends DomainException {

    public SlotNotAvailableException(String message) {
        super(message);
    }
}
