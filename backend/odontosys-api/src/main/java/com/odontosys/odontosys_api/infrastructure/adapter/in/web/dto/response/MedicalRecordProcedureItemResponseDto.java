package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordProcedureItemResponse;

public record MedicalRecordProcedureItemResponseDto(
        UUID id,
        UUID procedureId,
        String procedureName,
        BigDecimal appliedPrice,
        Integer toothNumber,
        String notes
) {
    public static MedicalRecordProcedureItemResponseDto fromApplication(MedicalRecordProcedureItemResponse response) {
        return new MedicalRecordProcedureItemResponseDto(
                response.id(),
                response.procedureId(),
                response.procedureName(),
                response.appliedPrice(),
                response.toothNumber(),
                response.notes()
        );
    }
}
