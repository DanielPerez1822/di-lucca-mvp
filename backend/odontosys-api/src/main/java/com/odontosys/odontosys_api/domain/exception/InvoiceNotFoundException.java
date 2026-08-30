package com.odontosys.odontosys_api.domain.exception;

/**
 * Se lanza cuando no se encuentra una factura.
 */
public class InvoiceNotFoundException extends DomainException {

    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
