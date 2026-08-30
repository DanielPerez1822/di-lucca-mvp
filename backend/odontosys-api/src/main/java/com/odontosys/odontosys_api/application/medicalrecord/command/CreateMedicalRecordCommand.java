package com.odontosys.odontosys_api.application.medicalrecord.command;

import java.util.List;
import java.util.UUID;

public record CreateMedicalRecordCommand(
        UUID patientId,
        UUID dentistId,
        UUID appointmentId,
        String diagnosis,
        String notes,
        List<MedicalRecordProcedureItemCommand> items
) {
}
