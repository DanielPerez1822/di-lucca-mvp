package com.odontosys.odontosys_api.domain.model;

/**
 * Roles del sistema OdontoSys.
 * <p>
 * No existe rol de paciente — los pacientes son datos gestionados
 * por el empleado/asistente, no usuarios del sistema.
 */
public enum Role {

    SECRETARY_ASSISTANT,
    DENTIST,
    ADMIN
}
