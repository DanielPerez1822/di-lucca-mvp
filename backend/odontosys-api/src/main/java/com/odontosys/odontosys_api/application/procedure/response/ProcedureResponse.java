package com.odontosys.odontosys_api.application.procedure.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Procedure;

public record ProcedureResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer duration,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProcedureResponse fromDomain(Procedure procedure) {
        return new ProcedureResponse(
                procedure.getId(),
                procedure.getName(),
                procedure.getDescription(),
                procedure.getPrice(),
                procedure.getDuration(),
                procedure.isActive(),
                procedure.getCreatedAt(),
                procedure.getUpdatedAt()
        );
    }
}
