package com.odontosys.odontosys_api.application.medicalrecord.command;

import java.math.BigDecimal;
import java.util.UUID;

public record MedicalRecordProcedureItemCommand(
        UUID procedureId,
        BigDecimal appliedPrice,
        Integer toothNumber,
        String notes
) {
}
