package com.odontosys.odontosys_api.application.patient.command;

import java.time.LocalDate;

public record UpdatePatientCommand(
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        String phone,
        String email,
        LocalDate dateOfBirth,
        String address
) {
}
