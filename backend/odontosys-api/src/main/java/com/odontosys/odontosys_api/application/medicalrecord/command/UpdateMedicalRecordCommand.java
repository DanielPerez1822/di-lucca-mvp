package com.odontosys.odontosys_api.application.medicalrecord.command;

import java.util.List;

public record UpdateMedicalRecordCommand(
        String diagnosis,
        String notes,
        List<MedicalRecordProcedureItemCommand> items
) {
}
