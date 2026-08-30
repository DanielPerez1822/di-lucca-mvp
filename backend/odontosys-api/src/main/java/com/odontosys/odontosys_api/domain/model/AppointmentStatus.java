package com.odontosys.odontosys_api.domain.model;

/**
 * Estados posibles de una cita odontológica.
 */
public enum AppointmentStatus {
    SCHEDULED,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
