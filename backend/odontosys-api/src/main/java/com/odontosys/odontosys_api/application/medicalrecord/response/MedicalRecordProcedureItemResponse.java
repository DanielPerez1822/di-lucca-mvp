package com.odontosys.odontosys_api.application.medicalrecord.response;

import java.math.BigDecimal;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.MedicalRecordProcedureItem;

public record MedicalRecordProcedureItemResponse(
        UUID id,
        UUID procedureId,
        String procedureName,
        BigDecimal appliedPrice,
        Integer toothNumber,
        String notes
) {
    public static MedicalRecordProcedureItemResponse fromDomain(MedicalRecordProcedureItem item) {
        return new MedicalRecordProcedureItemResponse(
                item.getId(),
                item.getProcedureId(),
                item.getProcedureName(),
                item.getAppliedPrice(),
                item.getToothNumber(),
                item.getNotes()
        );
    }
}
